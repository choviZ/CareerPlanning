package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.entity.PostFavorite;
import com.zcw.cpbackend.model.vo.PostVO;

/**
 * 用户收藏表 服务层。
 *
 * @author zcw
 */
public interface PostFavoriteService extends IService<PostFavorite> {

    /**
     * 收藏或取消收藏帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否收藏（true-收藏，false-取消收藏）
     */
    Boolean toggleFavorite(Long userId, Long postId);

    /**
     * 检查用户是否已收藏帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否已收藏
     */
    Boolean hasFavorited(Long userId, Long postId);

    /**
     * 获取用户收藏的帖子列表
     *
     * @param userId 用户ID
     * @param current 当前页
     * @param pageSize 页面大小
     * @return 收藏的帖子分页数据
     */
    Page<PostVO> getUserFavoritePosts(Long userId, Integer current, Integer pageSize);
}