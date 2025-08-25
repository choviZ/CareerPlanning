package com.zcw.cpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.constance.UserRoleConstance;
import com.zcw.cpbackend.model.dto.resume.ResumeAddRequest;
import com.zcw.cpbackend.model.dto.resume.ResumeQueryRequest;
import com.zcw.cpbackend.model.dto.resume.ResumeUpdateRequest;
import com.zcw.cpbackend.model.vo.ResumeVo;
import com.zcw.cpbackend.service.ResumeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 简历控制器
 *
 * @author zcw
 */
@RestController
@RequestMapping("/resume")
@Tag(name = "简历管理")
public class ResumeController {

    @Resource
    private ResumeService resumeService;

    /**
     * 添加简历
     *
     * @param resumeAddRequest 简历信息
     * @return 简历ID
     */
    @PostMapping("/add")
    @SaCheckLogin
    public BaseResponse<Long> addResume(@RequestBody ResumeAddRequest resumeAddRequest) {
        Long loginUserId = StpUtil.getLoginIdAsLong();
        Long resumeId = resumeService.addResume(resumeAddRequest, loginUserId);
        return ResultUtils.success(resumeId);
    }

    /**
     * 删除简历
     *
     * @param id 简历ID
     * @return 是否成功
     */
    @GetMapping("/delete/{id}")
    @SaCheckLogin
    public BaseResponse<Boolean> deleteResume(@PathVariable Long id) {
        Long loginUserId = StpUtil.getLoginIdAsLong();
        Boolean result = resumeService.deleteResume(id, loginUserId);
        return ResultUtils.success(result);
    }

    /**
     * 更新简历
     *
     * @param resumeUpdateRequest 简历更新信息
     * @return 是否成功
     */
    @PostMapping("/update")
    @SaCheckLogin
    public BaseResponse<Boolean> updateResume(@RequestBody ResumeUpdateRequest resumeUpdateRequest) {
        Long loginUserId = StpUtil.getLoginIdAsLong();
        Boolean result = resumeService.updateResume(resumeUpdateRequest, loginUserId);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询简历
     *
     * @param resumeQueryRequest 查询条件
     * @return 分页结果
     */
    @PostMapping("/list/page")
    @SaCheckLogin
    public BaseResponse<Page<ResumeVo>> listResumeByPage(@RequestBody ResumeQueryRequest resumeQueryRequest) {
        Long loginUserId = StpUtil.getLoginIdAsLong();
        Page<ResumeVo> result = resumeService.queryResume(resumeQueryRequest, loginUserId);
        return ResultUtils.success(result);
    }

    /**
     * 根据ID查询简历
     *
     * @param id 简历ID
     * @return 简历信息
     */
    @GetMapping("/get/{id}")
    @SaCheckLogin
    public BaseResponse<ResumeVo> getResumeById(@PathVariable Long id) {
        Long loginUserId = StpUtil.getLoginIdAsLong();
        ResumeVo result = resumeService.queryResumeById(id, loginUserId);
        return ResultUtils.success(result);
    }

    /**
     * 根据分享码查询简历（无需登录）
     *
     * @param shareCode 分享码
     * @return 简历信息
     */
    @GetMapping("/share/{shareCode}")
    public BaseResponse<ResumeVo> getResumeByShareCode(@PathVariable String shareCode) {
        ResumeVo result = resumeService.queryResumeByShareCode(shareCode);
        return ResultUtils.success(result);
    }

    /**
     * 复制简历
     *
     * @param id 原简历ID
     * @return 新简历ID
     */
    @PostMapping("/copy/{id}")
    @SaCheckLogin
    public BaseResponse<Long> copyResume(@PathVariable Long id) {
        Long loginUserId = StpUtil.getLoginIdAsLong();
        Long newResumeId = resumeService.copyResume(id, loginUserId);
        return ResultUtils.success(newResumeId);
    }

    /**
     * 生成分享码
     *
     * @param id 简历ID
     * @return 分享码
     */
    @PostMapping("/share/generate/{id}")
    @SaCheckLogin
    public BaseResponse<String> generateShareCode(@PathVariable Long id) {
        Long loginUserId = StpUtil.getLoginIdAsLong();
        String shareCode = resumeService.generateShareCode(id, loginUserId);
        return ResultUtils.success(shareCode);
    }

    /**
     * 发布简历
     *
     * @param id 简历ID
     * @return 是否成功
     */
    @PostMapping("/publish/{id}")
    @SaCheckLogin
    public BaseResponse<Boolean> publishResume(@PathVariable Long id) {
        Long loginUserId = StpUtil.getLoginIdAsLong();
        Boolean result = resumeService.publishResume(id, loginUserId);
        return ResultUtils.success(result);
    }

    // ==================== 管理员接口 ====================

    /**
     * 管理员分页查询简历
     *
     * @param resumeQueryRequest 查询条件
     * @return 分页结果
     */
    @PostMapping("/admin/list/page")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Page<ResumeVo>> adminListResumeByPage(@RequestBody ResumeQueryRequest resumeQueryRequest) {
        // 管理员查询时，传入null作为loginUserId，在Service中特殊处理
        Page<ResumeVo> result = resumeService.queryResume(resumeQueryRequest, null);
        return ResultUtils.success(result);
    }

    /**
     * 管理员根据ID查询简历
     *
     * @param id 简历ID
     * @return 简历信息
     */
    @GetMapping("/admin/get/{id}")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<ResumeVo> adminGetResumeById(@PathVariable Long id) {
        // 管理员查询时，传入null作为loginUserId，在Service中特殊处理
        ResumeVo result = resumeService.queryResumeById(id, null);
        return ResultUtils.success(result);
    }

    /**
     * 管理员删除简历
     *
     * @param id 简历ID
     * @return 是否成功
     */
    @GetMapping("/admin/delete/{id}")
    @SaCheckRole(UserRoleConstance.ADMIN)
    public BaseResponse<Boolean> adminDeleteResume(@PathVariable Long id) {
        // 管理员删除时，传入null作为loginUserId，在Service中特殊处理
        Boolean result = resumeService.deleteResume(id, null);
        return ResultUtils.success(result);
    }
}