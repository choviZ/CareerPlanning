package com.zcw.cpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.constance.UserRoleConstance;
import com.zcw.cpbackend.model.dto.asmtresult.AddAssessmentResultRequest;
import com.zcw.cpbackend.model.dto.asmtresult.QueryAssessmentResultRequest;
import com.zcw.cpbackend.model.dto.asmtresult.UpdateAssessmentResultRequest;
import com.zcw.cpbackend.model.vo.AssessmentResultVo;
import com.zcw.cpbackend.service.AssessmentResultService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 测评结果表 控制层。
 *
 * @author zcw
 */
@RestController
@RequestMapping("/assessmentResult")
public class AssessmentResultController {

    @Resource
    private AssessmentResultService assessmentResultService;

    /**
     * 添加测评结果
     *
     * @param addAssessmentResultRequest 测评结果信息
     * @return 是否成功
     */
    @PostMapping("/add")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Boolean> addAssessmentResult(@RequestBody AddAssessmentResultRequest addAssessmentResultRequest) {
        boolean result = assessmentResultService.addAssessmentResult(addAssessmentResultRequest);
        return ResultUtils.success(result);
    }

    /**
     * 删除测评结果
     *
     * @param id 测评结果id
     * @return 是否成功
     */
    @GetMapping("/delete/{id}")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Boolean> deleteAssessmentResult(@PathVariable Long id) {
        boolean result = assessmentResultService.deleteAssessmentResult(id);
        return ResultUtils.success(result);
    }

    /**
     * 更新测评结果
     *
     * @param updateAssessmentResultRequest 测评结果信息
     * @return 是否成功
     */
    @PostMapping("/update")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Boolean> updateAssessmentResult(@RequestBody UpdateAssessmentResultRequest updateAssessmentResultRequest) {
        boolean result = assessmentResultService.updateAssessmentResult(updateAssessmentResultRequest);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询测评结果列表
     *
     * @param queryAssessmentResultRequest 查询条件
     * @return 测评结果分页
     */
    @PostMapping("/list/page")
    @SaCheckRole(UserRoleConstance.USER)
    public BaseResponse<Page<AssessmentResultVo>> listAssessmentResultByPage(@RequestBody QueryAssessmentResultRequest queryAssessmentResultRequest) {
        Page<AssessmentResultVo> assessmentResultVoPage = assessmentResultService.queryAssessmentResult(queryAssessmentResultRequest);
        return ResultUtils.success(assessmentResultVoPage);
    }

    /**
     * 根据id查看测评结果
     *
     * @param id 测评结果id
     * @return 测评结果信息
     */
    @GetMapping("/get/{id}")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<AssessmentResultVo> getAssessmentResultById(@PathVariable Long id) {
        AssessmentResultVo assessmentResultVo = assessmentResultService.queryAssessmentResultById(id);
        return ResultUtils.success(assessmentResultVo);
    }

}
