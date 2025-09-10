package com.zcw.cpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.constance.UserRoleConstance;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.exception.ThrowUtils;
import com.zcw.cpbackend.model.dto.learningresource.LearningResourceAddRequest;
import com.zcw.cpbackend.model.dto.learningresource.LearningResourceEditRequest;
import com.zcw.cpbackend.model.dto.learningresource.LearningResourceQueryRequest;
import com.zcw.cpbackend.model.vo.LearningResourceVO;
import com.zcw.cpbackend.service.LearningResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 学习资源控制器
 *
 * @author zcw
 */
@RestController
@RequestMapping("/learningResource")
@Slf4j
@Tag(name = "学习资源管理")
public class LearningResourceController {

    @Resource
    private LearningResourceService learningResourceService;

    /**
     * 添加学习资源
     *
     * @param learningResourceAddRequest 学习资源添加请求
     * @return 学习资源ID
     */
    @PostMapping("/add")
    @SaCheckLogin
    @Operation(summary = "添加学习资源")
    public BaseResponse<Long> addLearningResource(@RequestBody LearningResourceAddRequest learningResourceAddRequest) {
        ThrowUtils.throwIf(learningResourceAddRequest == null, ErrorCode.PARAMS_ERROR);
        Long userId = StpUtil.getLoginIdAsLong();
        Long resourceId = learningResourceService.addLearningResource(learningResourceAddRequest);
        return ResultUtils.success(resourceId);
    }

    /**
     * 编辑学习资源
     *
     * @param learningResourceEditRequest 学习资源编辑请求
     * @return 是否成功
     */
    @PostMapping("/edit")
    @SaCheckLogin
    @Operation(summary = "编辑学习资源")
    public BaseResponse<Boolean> editLearningResource(@RequestBody LearningResourceEditRequest learningResourceEditRequest) {
        ThrowUtils.throwIf(learningResourceEditRequest == null, ErrorCode.PARAMS_ERROR);
        Long userId = StpUtil.getLoginIdAsLong();
        Boolean result = learningResourceService.editLearningResource(learningResourceEditRequest, userId);
        return ResultUtils.success(result);
    }

    /**
     * 删除学习资源
     *
     * @param resourceIdStr 学习资源ID字符串
     * @return 是否成功
     */
    @PostMapping("/delete/{resourceId}")
    @SaCheckLogin
    @Operation(summary = "删除学习资源")
    public BaseResponse<Boolean> deleteLearningResource(@PathVariable("resourceId") String resourceIdStr) {
        // 参数验证和转换
        Long resourceId;
        try {
            ThrowUtils.throwIf(resourceIdStr == null || resourceIdStr.trim().isEmpty() || "undefined".equals(resourceIdStr), 
                    ErrorCode.PARAMS_ERROR, "学习资源ID不能为空");
            resourceId = Long.parseLong(resourceIdStr);
            ThrowUtils.throwIf(resourceId <= 0, ErrorCode.PARAMS_ERROR, "学习资源ID必须为正数");
        } catch (NumberFormatException e) {
            ThrowUtils.throwIf(true, ErrorCode.PARAMS_ERROR, "学习资源ID格式不正确");
            return null; // 这行不会执行，但为了编译通过
        }
        
        Long userId = StpUtil.getLoginIdAsLong();
        Boolean result = learningResourceService.deleteLearningResource(resourceId, userId);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询学习资源
     *
     * @param learningResourceQueryRequest 查询请求
     * @return 学习资源分页数据
     */
    @PostMapping("/list/page")
    @Operation(summary = "分页查询学习资源")
    public BaseResponse<Page<LearningResourceVO>> queryLearningResourcePage(@RequestBody LearningResourceQueryRequest learningResourceQueryRequest) {
        ThrowUtils.throwIf(learningResourceQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<LearningResourceVO> resourcePage = learningResourceService.queryLearningResourcePage(learningResourceQueryRequest);
        return ResultUtils.success(resourcePage);
    }

    /**
     * 根据ID获取学习资源详情
     *
     * @param resourceIdStr 学习资源ID字符串
     * @return 学习资源详情
     */
    @GetMapping("/get/{resourceId}")
    @Operation(summary = "获取学习资源详情")
    public BaseResponse<LearningResourceVO> getLearningResourceById(@PathVariable("resourceId") String resourceIdStr) {
        // 参数验证和转换
        Long resourceId;
        try {
            ThrowUtils.throwIf(resourceIdStr == null || resourceIdStr.trim().isEmpty() || "undefined".equals(resourceIdStr), 
                    ErrorCode.PARAMS_ERROR, "学习资源ID不能为空");
            resourceId = Long.parseLong(resourceIdStr);
            ThrowUtils.throwIf(resourceId <= 0, ErrorCode.PARAMS_ERROR, "学习资源ID必须为正数");
        } catch (NumberFormatException e) {
            ThrowUtils.throwIf(true, ErrorCode.PARAMS_ERROR, "学习资源ID格式不正确");
            return null; // 这行不会执行，但为了编译通过
        }
        
        LearningResourceVO resourceVO = learningResourceService.getLearningResourceById(resourceId);
        // 增加浏览量
        learningResourceService.incrementViewCount(resourceId);
        return ResultUtils.success(resourceVO);
    }

    /**
     * 切换学习资源置顶状态（仅管理员）
     *
     * @param resourceIdStr 学习资源ID字符串
     * @return 是否成功
     */
    @PostMapping("/top/{resourceId}")
    @SaCheckRole(UserRoleConstance.ADMIN)
    @Operation(summary = "切换学习资源置顶状态")
    public BaseResponse<Boolean> toggleTopStatus(@PathVariable("resourceId") String resourceIdStr) {
        // 参数验证和转换
        Long resourceId;
        try {
            ThrowUtils.throwIf(resourceIdStr == null || resourceIdStr.trim().isEmpty() || "undefined".equals(resourceIdStr), 
                    ErrorCode.PARAMS_ERROR, "学习资源ID不能为空");
            resourceId = Long.parseLong(resourceIdStr);
            ThrowUtils.throwIf(resourceId <= 0, ErrorCode.PARAMS_ERROR, "学习资源ID必须为正数");
        } catch (NumberFormatException e) {
            ThrowUtils.throwIf(true, ErrorCode.PARAMS_ERROR, "学习资源ID格式不正确");
            return null; // 这行不会执行，但为了编译通过
        }
        
        Boolean result = learningResourceService.toggleTop(resourceId);
        return ResultUtils.success(result);
    }

    /**
     * 根据分类查询学习资源
     *
     * @param category 分类名称
     * @param current 当前页
     * @param pageSize 页面大小
     * @return 学习资源分页数据
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "根据分类查询学习资源")
    public BaseResponse<Page<LearningResourceVO>> getLearningResourcesByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        ThrowUtils.throwIf(category == null || category.trim().isEmpty(), ErrorCode.PARAMS_ERROR);
        Page<LearningResourceVO> resourcePage = learningResourceService.getLearningResourcesByCategory(category, current, pageSize);
        return ResultUtils.success(resourcePage);
    }

    /**
     * 搜索学习资源
     *
     * @param keyword 搜索关键词
     * @param current 当前页
     * @param pageSize 页面大小
     * @return 学习资源分页数据
     */
    @GetMapping("/search")
    @Operation(summary = "搜索学习资源")
    public BaseResponse<Page<LearningResourceVO>> searchLearningResources(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        ThrowUtils.throwIf(keyword == null || keyword.trim().isEmpty(), ErrorCode.PARAMS_ERROR);
        Page<LearningResourceVO> resourcePage = learningResourceService.searchLearningResources(keyword, current, pageSize);
        return ResultUtils.success(resourcePage);
    }
}
