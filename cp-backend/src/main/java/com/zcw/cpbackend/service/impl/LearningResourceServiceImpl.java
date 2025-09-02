package com.zcw.cpbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.exception.ThrowUtils;
import com.zcw.cpbackend.mapper.LearningResourceMapper;
import com.zcw.cpbackend.model.dto.learningresource.LearningResourceAddRequest;
import com.zcw.cpbackend.model.dto.learningresource.LearningResourceEditRequest;
import com.zcw.cpbackend.model.dto.learningresource.LearningResourceQueryRequest;

import com.zcw.cpbackend.model.entity.LearningResource;
import com.zcw.cpbackend.model.entity.User;
import com.zcw.cpbackend.model.vo.LearningResourceVO;
import com.zcw.cpbackend.model.vo.UserVO;
import com.zcw.cpbackend.service.LearningResourceService;
import com.zcw.cpbackend.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学习资源表 服务层实现。
 *
 * @author zcw
 */
@Service
@Slf4j
public class LearningResourceServiceImpl extends ServiceImpl<LearningResourceMapper, LearningResource> implements LearningResourceService {

    @Resource
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addLearningResource(LearningResourceAddRequest learningResourceAddRequest) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 参数校验
        ThrowUtils.throwIf(learningResourceAddRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(StrUtil.isBlank(learningResourceAddRequest.getTitle()), ErrorCode.PARAMS_ERROR, "标题不能为空");
        ThrowUtils.throwIf(learningResourceAddRequest.getTitle().length() > 200, ErrorCode.PARAMS_ERROR, "标题过长");
        ThrowUtils.throwIf(StrUtil.isBlank(learningResourceAddRequest.getContent()), ErrorCode.PARAMS_ERROR, "内容不能为空");
        ThrowUtils.throwIf(learningResourceAddRequest.getResourceType() == null, ErrorCode.PARAMS_ERROR, "资源类型不能为空");
        ThrowUtils.throwIf(learningResourceAddRequest.getContentType() == null, ErrorCode.PARAMS_ERROR, "内容类型不能为空");
        // 构建学习资源对象
        LearningResource learningResource = LearningResource.builder()
                .title(learningResourceAddRequest.getTitle())
                .content(learningResourceAddRequest.getContent())
                .summary(learningResourceAddRequest.getSummary())
                .resourceType(learningResourceAddRequest.getResourceType())
                .contentType(learningResourceAddRequest.getContentType())
                .category(learningResourceAddRequest.getCategory())
                .coverImage(learningResourceAddRequest.getCoverImage())
                .authorId(userId)
                .viewCount(0)
                .isTop(learningResourceAddRequest.getIsTop() != null ? learningResourceAddRequest.getIsTop() : 0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(0)
                .build();
        // 保存学习资源
        boolean saved = this.save(learningResource);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "学习资源创建失败");
        log.info("用户 {} 创建了学习资源 {}", userId, learningResource.getId());
        return learningResource.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editLearningResource(LearningResourceEditRequest learningResourceEditRequest, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(learningResourceEditRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(learningResourceEditRequest.getId() == null || learningResourceEditRequest.getId() <= 0, ErrorCode.PARAMS_ERROR, "学习资源ID不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(learningResourceEditRequest.getTitle()), ErrorCode.PARAMS_ERROR, "标题不能为空");
        ThrowUtils.throwIf(learningResourceEditRequest.getTitle().length() > 200, ErrorCode.PARAMS_ERROR, "标题过长");
        ThrowUtils.throwIf(StrUtil.isBlank(learningResourceEditRequest.getContent()), ErrorCode.PARAMS_ERROR, "内容不能为空");

        // 检查学习资源是否存在
        LearningResource existingResource = this.getById(learningResourceEditRequest.getId());
        ThrowUtils.throwIf(existingResource == null, ErrorCode.NOT_FOUND_ERROR, "学习资源不存在");
        ThrowUtils.throwIf(existingResource.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "学习资源已删除");

        // 权限校验（只有作者可以编辑）
        ThrowUtils.throwIf(!existingResource.getAuthorId().equals(userId), ErrorCode.NO_AUTH_ERROR, "无权限编辑此学习资源");

        // 更新学习资源
        LearningResource updateResource = LearningResource.builder()
                .id(learningResourceEditRequest.getId())
                .title(learningResourceEditRequest.getTitle())
                .content(learningResourceEditRequest.getContent())
                .summary(learningResourceEditRequest.getSummary())
                .resourceType(learningResourceEditRequest.getResourceType())
                .contentType(learningResourceEditRequest.getContentType())
                .category(learningResourceEditRequest.getCategory())
                .coverImage(learningResourceEditRequest.getCoverImage())
                .isTop(learningResourceEditRequest.getIsTop())
                .updatedAt(LocalDateTime.now())
                .build();
        boolean updated = this.updateById(updateResource);
        ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "学习资源更新失败");
        log.info("用户 {} 编辑了学习资源 {}", userId, learningResourceEditRequest.getId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteLearningResource(Long resourceId, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(resourceId == null || resourceId <= 0, ErrorCode.PARAMS_ERROR, "学习资源ID不能为空");
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.NOT_LOGIN_ERROR);
        // 检查学习资源是否存在
        LearningResource existingResource = this.getById(resourceId);
        ThrowUtils.throwIf(existingResource == null, ErrorCode.NOT_FOUND_ERROR, "学习资源不存在");
        ThrowUtils.throwIf(existingResource.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "学习资源已删除");
        // 权限校验（只有作者可以删除）
        ThrowUtils.throwIf(!existingResource.getAuthorId().equals(userId), ErrorCode.NO_AUTH_ERROR, "无权限删除此学习资源");
        // 逻辑删除
        LearningResource deleteResource = LearningResource.builder()
                .id(resourceId)
                .isDeleted(1)
                .updatedAt(LocalDateTime.now())
                .build();
        boolean deleted = this.updateById(deleteResource);
        ThrowUtils.throwIf(!deleted, ErrorCode.OPERATION_ERROR, "学习资源删除失败");
        log.info("用户 {} 删除了学习资源 {}", userId, resourceId);
        return true;
    }

    @Override
    public Page<LearningResourceVO> queryLearningResourcePage(LearningResourceQueryRequest learningResourceQueryRequest) {
        // 参数校验
        ThrowUtils.throwIf(learningResourceQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 构建查询条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(LearningResource::getIsDeleted, 0)
                .like(LearningResource::getTitle, learningResourceQueryRequest.getTitle(), StrUtil.isNotBlank(learningResourceQueryRequest.getTitle()))
                .like(LearningResource::getSummary, learningResourceQueryRequest.getSummary(), StrUtil.isNotBlank(learningResourceQueryRequest.getSummary()))
                .eq(LearningResource::getResourceType, learningResourceQueryRequest.getResourceType(), learningResourceQueryRequest.getResourceType() != null)
                .eq(LearningResource::getContentType, learningResourceQueryRequest.getContentType(), learningResourceQueryRequest.getContentType() != null)
                .eq(LearningResource::getCategory, learningResourceQueryRequest.getCategory(), StrUtil.isNotBlank(learningResourceQueryRequest.getCategory()))
                .eq(LearningResource::getAuthorId, learningResourceQueryRequest.getAuthorId(), learningResourceQueryRequest.getAuthorId() != null)
                .eq(LearningResource::getIsTop, learningResourceQueryRequest.getIsTop(), learningResourceQueryRequest.getIsTop() != null)
                .orderBy(LearningResource::getIsTop, false)
                .orderBy(LearningResource::getCreatedAt, false);
        // 关键词搜索
        if (StrUtil.isNotBlank(learningResourceQueryRequest.getKeyword())) {
            queryWrapper
                    .like(LearningResource::getTitle, learningResourceQueryRequest.getKeyword())
                    .like(LearningResource::getContent, learningResourceQueryRequest.getKeyword())
                    .like(LearningResource::getSummary, learningResourceQueryRequest.getKeyword());
        }

        // 分页查询
        Page<LearningResource> learningResourcePage = this.page(
                Page.of(learningResourceQueryRequest.getCurrent(), learningResourceQueryRequest.getPageSize()),
                queryWrapper
        );
        // 转换为VO
        return

                convertToLearningResourceVOPage(learningResourcePage);
    }

    @Override
    public LearningResourceVO getLearningResourceById(Long resourceId) {
        // 参数校验
        ThrowUtils.throwIf(resourceId == null || resourceId <= 0, ErrorCode.PARAMS_ERROR, "学习资源ID不能为空");

        // 查询学习资源
        LearningResource learningResource = this.getById(resourceId);
        ThrowUtils.throwIf(learningResource == null, ErrorCode.NOT_FOUND_ERROR, "学习资源不存在");
        ThrowUtils.throwIf(learningResource.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "学习资源已删除");

        // 转换为VO
        LearningResourceVO learningResourceVO = LearningResourceVO.objToVo(learningResource);

        // 设置作者信息
        if (learningResource.getAuthorId() != null) {
            User author = userService.getById(learningResource.getAuthorId());
            if (author != null) {
                learningResourceVO.setAuthor(UserVO.objToVo(author));
            }
        }

        return learningResourceVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long resourceId) {
        // 参数校验
        ThrowUtils.throwIf(resourceId == null || resourceId <= 0, ErrorCode.PARAMS_ERROR, "学习资源ID不能为空");
        // 增加浏览量
        QueryWrapper updateWrapper = QueryWrapper.create()
                .eq(LearningResource::getId, resourceId)
                .eq(LearningResource::getIsDeleted, 0);

        LearningResource updateResource = LearningResource.builder()
                .viewCount(1) // 这里应该使用SQL的增量更新，暂时简化处理
                .build();
        // 实际应该使用原生SQL: UPDATE learning_resource SET view_count = view_count + 1 WHERE id = ?
        LearningResource existingResource = this.getById(resourceId);
        if (existingResource != null && existingResource.getIsDeleted() == 0) {
            updateResource.setId(resourceId);
            updateResource.setViewCount(existingResource.getViewCount() + 1);
            this.updateById(updateResource);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean toggleTop(Long resourceId) {
        // 参数校验
        ThrowUtils.throwIf(resourceId == null || resourceId <= 0, ErrorCode.PARAMS_ERROR, "学习资源ID不能为空");
        // 查询当前状态
        LearningResource existingResource = this.getById(resourceId);
        ThrowUtils.throwIf(existingResource == null, ErrorCode.NOT_FOUND_ERROR, "学习资源不存在");
        ThrowUtils.throwIf(existingResource.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "学习资源已删除");
        // 切换置顶状态
        Integer newTopStatus = existingResource.getIsTop() == 1 ? 0 : 1;
        LearningResource updateResource = LearningResource.builder()
                .id(resourceId)
                .isTop(newTopStatus)
                .updatedAt(LocalDateTime.now())
                .build();
        boolean updated = this.updateById(updateResource);
        ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "置顶状态更新失败");
        log.info("学习资源 {} 置顶状态切换为 {}", resourceId, newTopStatus == 1 ? "置顶" : "取消置顶");
        return newTopStatus == 1;
    }

    @Override
    public Page<LearningResourceVO> getLearningResourcesByCategory(String category, Integer current, Integer pageSize) {
        // 参数校验
        ThrowUtils.throwIf(StrUtil.isBlank(category), ErrorCode.PARAMS_ERROR, "分类名称不能为空");
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(LearningResource::getCategory, category)
                .eq(LearningResource::getIsDeleted, 0)
                .orderBy(LearningResource::getIsTop, false)
                .orderBy(LearningResource::getCreatedAt, false);
        Page<LearningResource> learningResourcePage = this.page(Page.of(current, pageSize), queryWrapper);
        return convertToLearningResourceVOPage(learningResourcePage);
    }

    @Override
    public Page<LearningResourceVO> searchLearningResources(String keyword, Integer current, Integer pageSize) {
        // 参数校验
        ThrowUtils.throwIf(StrUtil.isBlank(keyword), ErrorCode.PARAMS_ERROR, "搜索关键词不能为空");
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(LearningResource.class)
                .eq(LearningResource::getIsDeleted, 0)
                .like(LearningResource::getTitle, keyword)
                .like(LearningResource::getSummary, keyword)
                .orderBy(LearningResource::getIsTop, false)
                .orderBy(LearningResource::getCreatedAt, false);
        Page<LearningResource> learningResourcePage = this.page(Page.of(current, pageSize), queryWrapper);
        return convertToLearningResourceVOPage(learningResourcePage);
    }

    /**
     * 转换为LearningResourceVO分页对象
     */
    private Page<LearningResourceVO> convertToLearningResourceVOPage(Page<LearningResource> learningResourcePage) {
        List<LearningResourceVO> learningResourceVOList = learningResourcePage.getRecords().stream()
                .map(learningResource -> {
                    LearningResourceVO learningResourceVO = LearningResourceVO.objToVo(learningResource);
                    // 设置作者信息
                    if (learningResource.getAuthorId() != null) {
                        User author = userService.getById(learningResource.getAuthorId());
                        if (author != null) {
                            learningResourceVO.setAuthor(UserVO.objToVo(author));
                        }
                    }
                    return learningResourceVO;
                })
                .collect(Collectors.toList());

        Page<LearningResourceVO> learningResourceVOPage = new Page<>();
        BeanUtils.copyProperties(learningResourcePage, learningResourceVOPage);
        learningResourceVOPage.setRecords(learningResourceVOList);
        return learningResourceVOPage;
    }

}
