package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.dto.asmtresult.AddAssessmentResultRequest;
import com.zcw.cpbackend.model.dto.asmtresult.QueryAssessmentResultRequest;
import com.zcw.cpbackend.model.dto.asmtresult.UpdateAssessmentResultRequest;
import com.zcw.cpbackend.model.entity.AssessmentResult;
import com.zcw.cpbackend.model.vo.AssessmentResultVo;

/**
 * 测评结果表 服务层。
 *
 * @author zcw
 */
public interface AssessmentResultService extends IService<AssessmentResult> {

    /**
     * 添加测评结果
     *
     * @param addAssessmentResultRequest 测评结果信息
     * @return 是否成功
     */
    boolean addAssessmentResult(AddAssessmentResultRequest addAssessmentResultRequest);

    /**
     * 删除测评结果
     *
     * @param id 测评结果id
     * @return 是否成功
     */
    boolean deleteAssessmentResult(Long id);

    /**
     * 更新测评结果
     *
     * @param updateAssessmentResultRequest 测评结果信息
     * @return 是否成功
     */
    boolean updateAssessmentResult(UpdateAssessmentResultRequest updateAssessmentResultRequest);

    /**
     * 分页查询测评结果
     *
     * @param queryAssessmentResultRequest 查询条件
     * @return 测评结果分页
     */
    Page<AssessmentResultVo> queryAssessmentResult(QueryAssessmentResultRequest queryAssessmentResultRequest);

    /**
     * 根据id查询测评结果
     *
     * @param id 测评结果id
     * @return 测评结果信息
     */
    AssessmentResultVo queryAssessmentResultById(Long id);

}
