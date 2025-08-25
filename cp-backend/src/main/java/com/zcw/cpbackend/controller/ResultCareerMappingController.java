package com.zcw.cpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.constance.UserRoleConstance;
import com.zcw.cpbackend.model.dto.assessment.UpdateResultCareerRequest;
import com.zcw.cpbackend.model.entity.ResultCareerMapping;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import com.zcw.cpbackend.service.ResultCareerMappingService;

/**
 * 测评结果职业关联表 控制层。
 *
 * @author zcw
 */
@RestController
@RequestMapping("/result-career")
public class ResultCareerMappingController {

    @Resource
    private ResultCareerMappingService resultCareerMappingService;

    /**
     * 添加测评结果职业关联
     *
     * @param assessmentResultId 测评结果id
     * @param careerId 职业id
     * @param compatibilityScore 兼容性评分
     * @return 是否成功
     */
    @PostMapping("/add")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Boolean> addResultCareerMapping(
            @RequestParam Long assessmentResultId,
            @RequestParam Long careerId,
            @RequestParam int compatibilityScore) {
        boolean result = resultCareerMappingService.addResultCareerMapping(assessmentResultId, careerId, compatibilityScore);
        return ResultUtils.success(result);
    }

    /**
     * 删除测评结果职业关联
     *
     * @param id 关联id
     * @return 是否成功
     */
    @GetMapping("/delete/{id}")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Boolean> deleteResultCareerMapping(@PathVariable Long id) {
        boolean result = resultCareerMappingService.deleteResultCareerMapping(id);
        return ResultUtils.success(result);
    }

    /**
     * 更新测评结果职业关联
     *
     * @param updateResultCareerRequest 更新请求
     * @return 是否成功
     */
    @PostMapping("/update")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Boolean> updateResultCareerMapping(@RequestBody UpdateResultCareerRequest updateResultCareerRequest) {
        boolean result = resultCareerMappingService.updateResultCareerMapping(updateResultCareerRequest);
        return ResultUtils.success(result);
    }

    /**
     * 根据测评结果查询结果职业关联表
     * 
     * @param testType 评估类型，可为null，表示查询所有类型
     * @param resultCode 评估结果，可为null，表示查询所有结果
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 结果职业关联表分页
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<ResultCareerMapping>> queryMappingByResultCode(
            @RequestParam(required = false) String testType,
            @RequestParam(required = false) String resultCode,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<ResultCareerMapping> mappingPage = resultCareerMappingService.queryMappingByResultCode(testType, resultCode, pageNum, pageSize);
        return ResultUtils.success(mappingPage);
    }

    /**
     * 查询最匹配的结果职业映射
     * 
     * @param testType 评估类型，可为null，表示查询所有类型
     * @param resultCode 评估结果，可为null，表示查询所有结果
     * @return 最匹配的结果职业映射
     */
    @GetMapping("/best-match")
    public BaseResponse<ResultCareerMapping> queryBestCompatibleCareer(
            @RequestParam(required = false) String testType,
            @RequestParam(required = false) String resultCode) {
        ResultCareerMapping mapping = resultCareerMappingService.queryBastCompatibleCareer(testType, resultCode);
        return ResultUtils.success(mapping);
    }
}
