package com.zcw.cpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.model.dto.resumetemplate.ResumeTemplateAddRequest;
import com.zcw.cpbackend.model.dto.resumetemplate.ResumeTemplateQueryRequest;
import com.zcw.cpbackend.model.dto.resumetemplate.ResumeTemplateUpdateRequest;
import com.zcw.cpbackend.model.vo.ResumeTemplateVo;
import com.zcw.cpbackend.service.ResumeTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 简历模板控制器
 *
 * @author zcw
 */
@RestController
@RequestMapping("/resumeTemplate")
@Slf4j
@Tag(name = "简历模板管理")
public class ResumeTemplateController {

    @Resource
    private ResumeTemplateService resumeTemplateService;

    /**
     * 添加简历模板（仅管理员）
     *
     * @param resumeTemplateAddRequest 添加请求
     * @return 模板ID
     */
    @PostMapping("/add")
    @SaCheckRole("admin")
    @Operation(summary = "添加简历模板")
    public BaseResponse<Long> addResumeTemplate(@RequestBody ResumeTemplateAddRequest resumeTemplateAddRequest) {
        Long templateId = resumeTemplateService.addResumeTemplate(resumeTemplateAddRequest);
        return ResultUtils.success(templateId);
    }

    /**
     * 删除简历模板（仅管理员）
     *
     * @param id 模板ID
     * @return 删除结果
     */
    @PostMapping("/delete")
    @SaCheckRole("admin")
    @Operation(summary = "删除简历模板")
    public BaseResponse<Boolean> deleteResumeTemplate(@RequestBody Long id) {
        Boolean result = resumeTemplateService.deleteResumeTemplate(id);
        return ResultUtils.success(result);
    }

    /**
     * 更新简历模板（仅管理员）
     *
     * @param resumeTemplateUpdateRequest 更新请求
     * @return 更新结果
     */
    @PostMapping("/update")
    @SaCheckRole("admin")
    @Operation(summary = "更新简历模板")
    public BaseResponse<Boolean> updateResumeTemplate(@RequestBody ResumeTemplateUpdateRequest resumeTemplateUpdateRequest) {
        Boolean result = resumeTemplateService.updateResumeTemplate(resumeTemplateUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询简历模板（仅管理员）
     *
     * @param resumeTemplateQueryRequest 查询请求
     * @return 分页结果
     */
    @PostMapping("/list/page")
    @SaCheckRole("admin")
    @Operation(summary = "分页查询简历模板")
    public BaseResponse<Page<ResumeTemplateVo>> queryResumeTemplate(@RequestBody ResumeTemplateQueryRequest resumeTemplateQueryRequest) {
        Page<ResumeTemplateVo> templatePage = resumeTemplateService.queryResumeTemplate(resumeTemplateQueryRequest);
        return ResultUtils.success(templatePage);
    }

    /**
     * 根据ID查询简历模板
     *
     * @param id 模板ID
     * @return 模板信息
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID查询简历模板")
    public BaseResponse<ResumeTemplateVo> queryResumeTemplateById(@PathVariable Long id) {
        ResumeTemplateVo templateVo = resumeTemplateService.queryResumeTemplateById(id);
        return ResultUtils.success(templateVo);
    }

    /**
     * 获取可用的简历模板列表
     *
     * @return 模板列表
     */
    @GetMapping("/available")
    @Operation(summary = "获取可用的简历模板列表")
    public BaseResponse<List<ResumeTemplateVo>> getAvailableTemplates() {
        List<ResumeTemplateVo> templates = resumeTemplateService.getAvailableTemplates();
        return ResultUtils.success(templates);
    }

    /**
     * 启用/禁用模板（仅管理员）
     *
     * @param id 模板ID
     * @param status 状态（0-禁用，1-启用）
     * @return 更新结果
     */
    @PostMapping("/status/{id}/{status}")
    @SaCheckRole("admin")
    @Operation(summary = "启用/禁用模板")
    public BaseResponse<Boolean> updateTemplateStatus(@PathVariable Long id, @PathVariable Integer status) {
        Boolean result = resumeTemplateService.updateTemplateStatus(id, status);
        return ResultUtils.success(result);
    }
}