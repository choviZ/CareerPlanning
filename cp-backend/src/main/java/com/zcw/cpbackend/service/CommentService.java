package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.dto.comment.CommentAddRequest;
import com.zcw.cpbackend.model.dto.comment.CommentDeleteRequest;
import com.zcw.cpbackend.model.dto.comment.CommentQueryRequest;
import com.zcw.cpbackend.model.entity.Comment;
import com.zcw.cpbackend.model.vo.CommentVO;

import java.util.List;

/**
 * 评论表 服务层。
 *
 * @author zcw
 */
public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     *
     * @param commentAddRequest 评论添加请求
     * @return 评论ID
     */
    Long addComment(CommentAddRequest commentAddRequest);

    /**
     * 删除评论（逻辑删除）
     *
     * @param commentDeleteRequest 评论删除请求
     * @return 是否成功
     */
    Boolean deleteComment(CommentDeleteRequest commentDeleteRequest);

    /**
     * 分页查询评论（支持层级结构）
     *
     * @param commentQueryRequest 查询请求
     * @return 评论分页数据
     */
    Page<CommentVO> queryCommentPage(CommentQueryRequest commentQueryRequest);

    /**
     * 获取帖子的评论列表（两层结构：顶级评论+二级回复）
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    List<CommentVO> getPostComments(Long postId);

    /**
     * 获取某个评论的所有回复
     *
     * @param commentId 评论ID
     * @return 回复列表
     */
    List<CommentVO> getCommentReplies(Long commentId);

    /**
     * 点赞/取消点赞评论
     *
     * @param commentId 评论ID
     * @return 是否成功
     */
    Boolean likeComment(Long commentId);

    /**
     * 检查用户是否已点赞评论
     *
     * @param commentId 评论ID
     * @return 是否已点赞
     */
    Boolean hasLikedComment(Long commentId);

    /**
     * 增加评论回复数
     *
     * @param commentId 评论ID
     * @return 是否成功
     */
    Boolean incrementReplyCount(Long commentId);

    /**
     * 减少评论回复数
     *
     * @param commentId 评论ID
     * @return 是否成功
     */
    Boolean decrementReplyCount(Long commentId);
}
