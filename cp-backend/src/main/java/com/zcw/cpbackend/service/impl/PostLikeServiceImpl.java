package com.zcw.cpbackend.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.exception.ThrowUtils;
import com.zcw.cpbackend.mapper.PostLikeMapper;
import com.zcw.cpbackend.model.entity.PostLike;
import com.zcw.cpbackend.service.PostLikeService;
import com.zcw.cpbackend.service.PostService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户点赞表 服务层实现。
 *
 * @author zcw
 */
@Service
public class PostLikeServiceImpl extends ServiceImpl<PostLikeMapper, PostLike> implements PostLikeService {

    @Resource
    private PostService postService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean toggleLike(Long userId, Integer targetType, Long targetId) {
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(targetType == null || (targetType != 1 && targetType != 2), ErrorCode.PARAMS_ERROR, "目标类型错误");
        ThrowUtils.throwIf(targetId == null || targetId <= 0, ErrorCode.PARAMS_ERROR, "目标ID不能为空");

        // 检查是否已点赞
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(PostLike::getUserId, userId)
                .eq(PostLike::getTargetType, targetType)
                .eq(PostLike::getTargetId, targetId);

        PostLike existingLike = this.getOne(queryWrapper);

        if (existingLike != null) {
            // 已点赞，取消点赞
            this.removeById(existingLike.getId());
            
            // 更新帖子点赞数（如果是帖子）
            if (targetType == 1) {
                postService.updatePostStats(targetId, -1, null);
            }
            return false;
        } else {
            // 未点赞，添加点赞
            PostLike postLike = PostLike.builder()
                    .userId(userId)
                    .targetType(targetType)
                    .targetId(targetId)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            this.save(postLike);
            
            // 更新帖子点赞数（如果是帖子）
            if (targetType == 1) {
                postService.updatePostStats(targetId, 1, null);
            }
            return true;
        }
    }

    @Override
    public Boolean hasLiked(Long userId, Integer targetType, Long targetId) {
        if (userId == null || userId <= 0) {
            return false;
        }
        
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(PostLike::getUserId, userId)
                .eq(PostLike::getTargetType, targetType)
                .eq(PostLike::getTargetId, targetId);

        return this.exists(queryWrapper);
    }

    @Override
    public Long getLikeCount(Integer targetType, Long targetId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(PostLike::getTargetType, targetType)
                .eq(PostLike::getTargetId, targetId);

        return this.count(queryWrapper);
    }
}