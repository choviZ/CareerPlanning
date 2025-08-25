package com.zcw.cpbackend.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.BusinessException;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.mapper.AssessmentResultMapper;
import com.zcw.cpbackend.model.dto.asmtresult.AddAssessmentResultRequest;
import com.zcw.cpbackend.model.dto.asmtresult.QueryAssessmentResultRequest;
import com.zcw.cpbackend.model.dto.asmtresult.UpdateAssessmentResultRequest;
import com.zcw.cpbackend.model.entity.AssessmentResult;
import com.zcw.cpbackend.model.vo.AssessmentResultVo;
import com.zcw.cpbackend.service.AssessmentResultService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 测评结果表 服务层实现。
 *
 * @author zcw
 */
@Service
public class AssessmentResultServiceImpl extends ServiceImpl<AssessmentResultMapper, AssessmentResult> implements AssessmentResultService {

    @Override
    public boolean addAssessmentResult(AddAssessmentResultRequest addAssessmentResultRequest) {
        if (addAssessmentResultRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String testType = addAssessmentResultRequest.getTestType();
        String resultCode = addAssessmentResultRequest.getResultCode();
        String resultName = addAssessmentResultRequest.getResultName();

        if (StringUtils.isBlank(testType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "测评类型不能为空");
        }
        if (StringUtils.isBlank(resultCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "结果代码不能为空");
        }
        if (StringUtils.isBlank(resultName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "结果名称不能为空");
        }

        // 创建测评结果对象
        AssessmentResult assessmentResult = new AssessmentResult();
        assessmentResult.setTestType(testType);
        assessmentResult.setResultCode(resultCode);
        assessmentResult.setResultName(resultName);
        assessmentResult.setResultDesc(addAssessmentResultRequest.getResultDesc());
        assessmentResult.setCreatedAt(LocalDateTime.now());
        assessmentResult.setUpdatedAt(LocalDateTime.now());

        // 保存到数据库
        return save(assessmentResult);
    }

    @Override
    public boolean deleteAssessmentResult(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 检查测评结果是否存在
        AssessmentResult assessmentResult = getById(id);
        if (assessmentResult == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 从数据库中删除
        return removeById(id);
    }

    @Override
    public boolean updateAssessmentResult(UpdateAssessmentResultRequest updateAssessmentResultRequest) {
        if (updateAssessmentResultRequest == null || updateAssessmentResultRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = updateAssessmentResultRequest.getId();
        // 检查测评结果是否存在
        AssessmentResult oldAssessmentResult = getById(id);
        if (oldAssessmentResult == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 更新测评结果信息
        AssessmentResult assessmentResult = new AssessmentResult();
        assessmentResult.setId(id);
        assessmentResult.setTestType(updateAssessmentResultRequest.getTestType());
        assessmentResult.setResultCode(updateAssessmentResultRequest.getResultCode());
        assessmentResult.setResultName(updateAssessmentResultRequest.getResultName());
        assessmentResult.setResultDesc(updateAssessmentResultRequest.getResultDesc());
        assessmentResult.setUpdatedAt(LocalDateTime.now());

        return updateById(assessmentResult);
    }

    /**
     * 构建查询条件
     *
     * @param queryAssessmentResultRequest 查询请求
     * @return 查询条件包装器
     */
    private QueryWrapper buildQueryWrapper(QueryAssessmentResultRequest queryAssessmentResultRequest) {
        QueryWrapper queryWrapper = QueryWrapper.create();

        String testType = queryAssessmentResultRequest.getTestType();
        if (StringUtils.isNotBlank(testType)) {
            queryWrapper.like(AssessmentResult::getTestType, testType);
        }

        String resultCode = queryAssessmentResultRequest.getResultCode();
        if (StringUtils.isNotBlank(resultCode)) {
            queryWrapper.like(AssessmentResult::getResultCode, resultCode);
        }

        String resultName = queryAssessmentResultRequest.getResultName();
        if (StringUtils.isNotBlank(resultName)) {
            queryWrapper.like(AssessmentResult::getResultName, resultName);
        }

        String resultDesc = queryAssessmentResultRequest.getResultDesc();
        if (StringUtils.isNotBlank(resultDesc)) {
            queryWrapper.like(AssessmentResult::getResultDesc, resultDesc);
        }

        // 默认按创建时间降序排序
        queryWrapper.orderBy(AssessmentResult::getCreatedAt, false);

        return queryWrapper;
    }

    @Override
    public Page<AssessmentResultVo> queryAssessmentResult(QueryAssessmentResultRequest queryAssessmentResultRequest) {
        if (queryAssessmentResultRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 构建查询条件
        QueryWrapper queryWrapper = buildQueryWrapper(queryAssessmentResultRequest);

        // 分页查询
        long current = queryAssessmentResultRequest.getCurrent();
        long pageSize = queryAssessmentResultRequest.getPageSize();
        Page<AssessmentResult> assessmentResultPage = page(new Page<>(current, pageSize), queryWrapper);

        // 转换为 AssessmentResultVo
        Page<AssessmentResultVo> assessmentResultVoPage = new Page<>(assessmentResultPage.getPageNumber(), assessmentResultPage.getPageSize(), assessmentResultPage.getTotalRow());
        List<AssessmentResultVo> assessmentResultVos = assessmentResultPage.getRecords().stream()
                .map(AssessmentResultVo::objToVo)
                .collect(Collectors.toList());

        assessmentResultVoPage.setRecords(assessmentResultVos);
        return assessmentResultVoPage;
    }

    @Override
    public AssessmentResultVo queryAssessmentResultById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return AssessmentResultVo.objToVo(getById(id));
    }

}
