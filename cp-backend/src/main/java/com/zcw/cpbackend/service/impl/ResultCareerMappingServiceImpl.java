package com.zcw.cpbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.exception.ThrowUtils;
import com.zcw.cpbackend.model.dto.assessment.UpdateResultCareerRequest;
import com.zcw.cpbackend.model.entity.AssessmentResult;
import com.zcw.cpbackend.model.entity.Career;
import com.zcw.cpbackend.model.entity.ResultCareerMapping;
import com.zcw.cpbackend.mapper.ResultCareerMappingMapper;
import com.zcw.cpbackend.model.enums.TestTypeEnum;
import com.zcw.cpbackend.service.AssessmentResultService;
import com.zcw.cpbackend.service.CareerService;
import com.zcw.cpbackend.service.ResultCareerMappingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 测评结果职业关联表 服务层实现。
 *
 * @author zcw
 */
@Service
public class ResultCareerMappingServiceImpl extends ServiceImpl<ResultCareerMappingMapper, ResultCareerMapping> implements ResultCareerMappingService {

    @Resource
    private AssessmentResultService assessmentResultService;
    @Resource
    private CareerService careerService;

    @Override
    public boolean addResultCareerMapping(Long assessmentResultId, Long careerId, int compatibilityScore) {
        // 查询出测评结果和职业
        AssessmentResult assessmentResult = assessmentResultService.getById(assessmentResultId);
        ThrowUtils.throwIf(assessmentResult == null, ErrorCode.NOT_FOUND_ERROR, "测评结果不存在");
        Career career = careerService.getById(careerId);
        ThrowUtils.throwIf(career == null, ErrorCode.NOT_FOUND_ERROR, "职业不存在");
        // 构造
        ResultCareerMapping resultCareerMapping = ResultCareerMapping.builder()
                .testType(assessmentResult.getTestType())
                .resultCode(assessmentResult.getResultCode())
                .careerId(careerId)
                .careerName(career.getName())
                .description(career.getDescription())
                .compatibilityScore(compatibilityScore)
                .createdAt(LocalDateTime.now())
                .build();
        return this.save(resultCareerMapping);
    }

    @Override
    public boolean updateResultCareerMapping(UpdateResultCareerRequest updateResultCareerRequest) {
        ThrowUtils.throwIf(updateResultCareerRequest == null, ErrorCode.PARAMS_ERROR);
        long id = updateResultCareerRequest.getId();
        long assessmentResultId = updateResultCareerRequest.getAssessmentResultId();
        long careerId = updateResultCareerRequest.getCareerId();
        int compatibilityScore = updateResultCareerRequest.getCompatibilityScore();
        // 查询原始数据
        ResultCareerMapping resultCareerMapping = this.getById(id);
        ThrowUtils.throwIf(resultCareerMapping == null, ErrorCode.NOT_FOUND_ERROR, "数据不存在");
        // 更新
        if (!resultCareerMapping.getCareerId().equals(careerId)) {
            // 更新职业
            Career career = careerService.getById(careerId);
            resultCareerMapping.setCareerId(careerId);
            resultCareerMapping.setCareerName(career.getName());
            resultCareerMapping.setDescription(career.getDescription());
        }
        if (!resultCareerMapping.getResultId().equals(assessmentResultId)) {
            // 更新测评结果
            AssessmentResult assessmentResult = assessmentResultService.getById(assessmentResultId);
            resultCareerMapping.setResultId(assessmentResultId);
            resultCareerMapping.setTestType(assessmentResult.getTestType());
            resultCareerMapping.setResultCode(assessmentResult.getResultCode());
        }
        resultCareerMapping.setCompatibilityScore(compatibilityScore);
        return this.updateById(resultCareerMapping);
    }

    @Override
    public boolean deleteResultCareerMapping(Long id) {
        ResultCareerMapping resultCareerMapping = this.getById(id);
        ThrowUtils.throwIf(resultCareerMapping == null, ErrorCode.NOT_FOUND_ERROR, "数据不存在");
        return this.removeById(id);
    }

    @Override
    public Page<ResultCareerMapping> queryMappingByResultCode(String testType, String resultCode, int pageNum, int pageSize) {
        // 校验
        TestTypeEnum typeEnum = TestTypeEnum.getEnumByValue(testType);
        ThrowUtils.throwIf(typeEnum == null, ErrorCode.PARAMS_ERROR, "评估类型错误");
        ThrowUtils.throwIf(StrUtil.isBlank(resultCode), ErrorCode.PARAMS_ERROR, "评估结果不能为空");
        // 构造查询条件
        QueryWrapper queryWrapper = new QueryWrapper()
                .eq(ResultCareerMapping::getTestType, testType)
                .eq(ResultCareerMapping::getResultCode, resultCode);
        // 分页查询
        return this.page(Page.of(pageNum, pageSize),queryWrapper);
    }

    @Override
    public ResultCareerMapping queryBastCompatibleCareer(String testType, String resultCode) {
        // 构造查询条件
        QueryWrapper queryWrapper = new QueryWrapper()
                .eq(ResultCareerMapping::getTestType, testType)
                .eq(ResultCareerMapping::getResultCode, resultCode)
                .orderBy(ResultCareerMapping::getCompatibilityScore,false)
                .limit(1);
        return this.getOne(queryWrapper);
    }
}
