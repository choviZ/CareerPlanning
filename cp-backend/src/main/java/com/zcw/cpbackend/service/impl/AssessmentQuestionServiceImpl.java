package com.zcw.cpbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.BusinessException;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.exception.ThrowUtils;
import com.zcw.cpbackend.service.AssessmentResultService;
import com.zcw.cpbackend.model.dto.assessment.AddQuestionRequest;
import com.zcw.cpbackend.model.dto.assessment.OptionDTO;
import com.zcw.cpbackend.model.dto.assessment.UpdateQuestionRequest;
import com.zcw.cpbackend.model.entity.*;
import com.zcw.cpbackend.mapper.AssessmentQuestionMapper;
import com.zcw.cpbackend.model.enums.TestTypeEnum;
import com.zcw.cpbackend.model.vo.AssessmentQuestionVo;
import com.zcw.cpbackend.model.vo.UserAssessmentVo;
import com.zcw.cpbackend.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 测评题目表 服务层实现。
 *
 * @author zcw
 */
@Slf4j
@Service
public class AssessmentQuestionServiceImpl extends ServiceImpl<AssessmentQuestionMapper, AssessmentQuestion> implements AssessmentQuestionService {

    @Resource
    private AssessmentResultService assessmentResultService;
    @Resource
    private ResultCareerMappingService resultCareerMappingService;
    @Resource
    private UserAssessmentService userAssessmentService;

    @Override
    public UserAssessmentVo doAssessment(List<String> userAnswers, String testType) {
        // 校验
        TestTypeEnum testTypeEnum = TestTypeEnum.getEnumByValue(testType);
        ThrowUtils.throwIf(testTypeEnum == null, ErrorCode.PARAMS_ERROR, "测评类型错误");
        // 评估-mbti
        // 记录每个维度的得分
        HashMap<String, Integer> map = new HashMap<>();
        // 1. 查询到题目列表
        List<AssessmentQuestionVo> questionList = this.queryQuestion(testType);
        // 2. 遍历用户答案
        for (int i = 0; i < questionList.size(); i++) {
            // 当前题目的全部选项
            List<OptionDTO> options = questionList.get(i).getOptions();
            for (OptionDTO option : options) {
                // 匹配用户的选项
                if (option.getKey().equals(userAnswers.get(i))) {
                    String dimension = option.getDimension();
                    // 该选项偏向中立，不做处理
                    if (dimension == null) {
                        continue;
                    }
                    Integer score = map.getOrDefault(dimension, 0);
                    map.put(dimension, score + 1);
                }
            }
        }
        // 3. 计算结果
        String dimensionScores = JSONUtil.toJsonStr(map);
        String result = calculateMBTI(map);
        // 查询结果表
        QueryWrapper queryWrapper = assessmentResultService.query().eq(AssessmentResult::getResultCode, result);
        AssessmentResult assessmentResult = assessmentResultService.getOne(queryWrapper);
        // 查询结果关联的职业
        // TODO 暂时只查询匹配度最高的一条记录
        ResultCareerMapping resultCareerMapping = resultCareerMappingService.queryBastCompatibleCareer(testType, assessmentResult.getResultCode());
        ThrowUtils.throwIf(resultCareerMapping == null, ErrorCode.NOT_FOUND_ERROR,"该评估结果没有对应的职业");
        // 4. 保存答题记录
        UserAssessment userAssessment = UserAssessment.builder()
                .testType(testType)
                .resultCode(assessmentResult.getResultCode())
                .resultName(assessmentResult.getResultName())
                .resultDesc(assessmentResult.getResultDesc())
                .userId(StpUtil.getLoginIdAsLong())
                .choices(JSONUtil.toJsonStr(userAnswers))
                .careerId(resultCareerMapping.getCareerId())
                .careerName(resultCareerMapping.getCareerName())
                .careerDescription(resultCareerMapping.getDescription())
                .dimensionScores(JSONUtil.toJsonStr(dimensionScores))
                .createdAt(LocalDateTime.now())
                .build();
        boolean saved = userAssessmentService.save(userAssessment);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR,"评估记录保存失败");
        return UserAssessmentVo.objToVo(userAssessment);
    }

    /**
     * 计算MBTI结果
     * @param map
     * @return
     */
    public String calculateMBTI(HashMap<String, Integer> map) {
        // 能量获取维度：E - 外向，I - 内向
        String ei = map.get("E") > map.get("I") ? "E" : "I";
        // 信息收集维度：S - 实感，N - 直觉
        String sn = map.get("S") > map.get("N") ? "S" : "N";
        // 决策方式维度：T - 思考，F - 情感
        String tf = map.get("T") > map.get("F") ? "T" : "F";
        // 生活方式维度：J - 判断，P - 知觉
        String jp = map.get("J") > map.get("P") ? "J" : "P";
        return ei + sn + tf + jp;
    }

    @Override
    public boolean addQuestion(AddQuestionRequest request) {
        // TODO 校验
        log.info("添加题目: {}", request.getContent());
        // 将请求参数转换为实体对象
        AssessmentQuestion question = AssessmentQuestion.builder()
                .testType(request.getTestType())
                .content(request.getContent())
                .dimension(request.getDimension())
                .options(JSONUtil.toJsonStr(request.getOptions()))
                .sortOrder(request.getSortOrder())
                .status(request.getStatus())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        // 保存到数据库
        boolean result = this.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "题目保存失败");
        return true;
    }

    @Override
    public boolean updateQuestion(UpdateQuestionRequest updateQuestionRequest) {
        log.info("修改题目: {}", updateQuestionRequest.getId());
        // 根据ID查询题目是否存在
        AssessmentQuestion existingQuestion = this.getById(updateQuestionRequest.getId());
        if (existingQuestion == null) {
            log.warn("题目不存在，ID: {}", updateQuestionRequest.getId());
            return false;
        }
        // 更新题目信息
        existingQuestion.setTestType(updateQuestionRequest.getTestType());
        existingQuestion.setContent(updateQuestionRequest.getContent());
        existingQuestion.setDimension(updateQuestionRequest.getDimension());
        existingQuestion.setOptions(updateQuestionRequest.getOptions());
        existingQuestion.setSortOrder(updateQuestionRequest.getSortOrder());
        existingQuestion.setStatus(updateQuestionRequest.getStatus());
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        // 更新到数据库
        boolean result = this.updateById(existingQuestion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "题目修改失败");
        return true;
    }

    @Override
    public boolean deleteQuestion(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        // 根据ID查询题目是否存在
        AssessmentQuestion existingQuestion = this.getById(id);
        ThrowUtils.throwIf(existingQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        // 从数据库中删除
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "题目删除失败");
        log.info("删除题目: {}", id);
        return true;
    }

    @Override
    public List<AssessmentQuestionVo> queryQuestion(String testType) {
        // 校验
        TestTypeEnum typeEnum = TestTypeEnum.getEnumByValue(testType);
        ThrowUtils.throwIf(typeEnum == null, ErrorCode.PARAMS_ERROR, "测试类型错误");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(AssessmentQuestion::getTestType, testType);
        List<AssessmentQuestion> questionList = this.list(queryWrapper);
        // 解析 option 构造Vo
        return toVoList(questionList);
    }

    /**
     * 转换为VO
     *
     * @param questionList 原始对象列表
     * @return vo列表
     */
    public List<AssessmentQuestionVo> toVoList(List<AssessmentQuestion> questionList) {
        if (CollUtil.isEmpty(questionList)) return Collections.emptyList();
        return questionList.stream()
                .map(question -> {
                    AssessmentQuestionVo vo = null;
                    try {
                        String jsonOptions = question.getOptions();
                        List<OptionDTO> options = JSONUtil.toList(jsonOptions, OptionDTO.class);
                        vo = AssessmentQuestionVo.objToVo(question);
                        vo.setOptions(options);
                    } catch (Exception e) {
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "JSON转换错误" + e.getMessage());
                    }
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
