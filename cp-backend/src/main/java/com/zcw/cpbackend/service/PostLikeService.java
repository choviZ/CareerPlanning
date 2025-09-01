package com.zcw.cpbackend.service;

import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.entity.PostLike;

/**
 * 用户点赞表 服务层。
 *
 * @author zcw
 */
public interface PostLikeService extends IService<PostLike> {

    /**
     * 点赞或取消点赞
     *
     * @param userId 用户ID
     * @param targetType 目标类型：1-帖子，2-评论
     * @param targetId 目标ID
     * @return 是否点赞（true-点赞，false-取消点赞）
     */
    Boolean toggleLike(Long userId, Integer targetType, Long targetId);

    /**
     * 检查用户是否已点赞
     *
     * @param userId 用户ID
     * @param targetType 目标类型：1-帖子，2-评论
     * @param targetId 目标ID
     * @return 是否已点赞
     */
    Boolean hasLiked(Long userId, Integer targetType, Long targetId);

    /**
     * 获取目标的点赞数量
     *
     * @param targetType 目标类型：1-帖子，2-评论
     * @param targetId 目标ID
     * @return 点赞数量
     */
    Long getLikeCount(Integer targetType, Long targetId);
}