package com.zcw.cpbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.BusinessException;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.exception.ThrowUtils;
import com.zcw.cpbackend.model.dto.AddQuestionRequest;
import com.zcw.cpbackend.model.dto.OptionDTO;
import com.zcw.cpbackend.model.dto.UpdateQuestionRequest;
import com.zcw.cpbackend.model.entity.AssessmentQuestion;
import com.zcw.cpbackend.mapper.AssessmentQuestionMapper;
import com.zcw.cpbackend.model.enums.TestTypeEnum;
import com.zcw.cpbackend.model.vo.AssessmentQuestionVo;
import com.zcw.cpbackend.model.vo.AssessmentResultVo;
import com.zcw.cpbackend.service.AssessmentQuestionService;
import com.zcw.cpbackend.service.AssessmentResultService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public AssessmentResultVo doAssessment(List<String> userAnswers, String testType) {
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
        String result = calculateResult(map);
        // 4. 保存答题记录
        AssessmentResultVo resultVo = AssessmentResultVo.builder()
                // TODO userId
                .userId(1L)
                .testType(testType)
                .resultCode(result)
                .dimensionScores(dimensionScores)
                .createdAt(LocalDateTime.now())
                .build();
        boolean saved = assessmentResultService.save(AssessmentResultVo.voToObj(resultVo));
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "保存测评记录失败");
        return resultVo;
    }

    private String calculateResult(Map<String, Integer> map) {
        // 按维度数量降序排序
        List<Map.Entry<String, Integer>> sortedDimensions = map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .toList();
        // 检查是否有足够的维度
        if (sortedDimensions.size() < 4) {
            throw new IllegalArgumentException("维度数量不足，无法生成MBTI类型");
        }
        // 取数量最高的4个维度拼接结果
        StringBuilder mbtiResult = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            mbtiResult.append(sortedDimensions.get(i).getKey());
        }
        return mbtiResult.toString();
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
                .options(request.getOptions())
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
