package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.dto.post.PostAddRequest;
import com.zcw.cpbackend.model.dto.post.PostEditRequest;
import com.zcw.cpbackend.model.dto.post.PostQueryRequest;
import com.zcw.cpbackend.model.entity.Post;
import com.zcw.cpbackend.model.vo.PostVO;

/**
 * 帖子表 服务层。
 *
 * @author zcw
 */
public interface PostService extends IService<Post> {

    /**
     * 发布帖子
     *
     * @param postAddRequest 帖子添加请求
     * @param userId 用户ID
     * @return 帖子ID
     */
    Long addPost(PostAddRequest postAddRequest, Long userId);

    /**
     * 编辑帖子
     *
     * @param postEditRequest 帖子编辑请求
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean editPost(PostEditRequest postEditRequest, Long userId);

    /**
     * 删除帖子（逻辑删除）
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean deletePost(Long postId, Long userId);

    /**
     * 分页查询帖子
     *
     * @param postQueryRequest 查询请求
     * @return 帖子分页数据
     */
    Page<PostVO> queryPostPage(PostQueryRequest postQueryRequest);

    /**
     * 分页查询帖子（支持传入当前用户ID）
     *
     * @param postQueryRequest 查询请求
     * @param currentUserId 当前用户ID
     * @return 帖子分页数据
     */
    Page<PostVO> queryPostPage(PostQueryRequest postQueryRequest, Long currentUserId);

    /**
     * 根据ID获取帖子详情
     *
     * @param postId 帖子ID
     * @param userId 当前用户ID（可为空）
     * @return 帖子详情
     */
    PostVO getPostById(Long postId, Long userId);

    /**
     * 查询用户发布的帖子
     *
     * @param userId 用户ID
     * @param current 当前页
     * @param pageSize 页面大小
     * @return 帖子分页数据
     */
    Page<PostVO> getUserPosts(Long userId, Integer current, Integer pageSize);

    /**
     * 增加帖子浏览量
     *
     * @param postId 帖子ID
     */
    void incrementViewCount(Long postId);

    /**
     * 更新帖子统计数据（点赞数、评论数）
     *
     * @param postId 帖子ID
     * @param likeCount 点赞数变化
     * @param commentCount 评论数变化
     */
    void updatePostStats(Long postId, Integer likeCount, Integer commentCount);
}
