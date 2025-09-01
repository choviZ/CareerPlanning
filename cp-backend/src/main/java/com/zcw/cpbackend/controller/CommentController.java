package com.zcw.cpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.exception.ThrowUtils;
import com.zcw.cpbackend.model.dto.comment.CommentAddRequest;
import com.zcw.cpbackend.model.dto.comment.CommentDeleteRequest;
import com.zcw.cpbackend.model.dto.comment.CommentQueryRequest;
import com.zcw.cpbackend.model.vo.CommentVO;
import com.zcw.cpbackend.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论表 控制层。
 *
 * @author zcw
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 添加评论
     *
     * @param commentAddRequest 评论添加请求
     * @return 评论ID
     */
    @PostMapping("/add")
    @SaCheckLogin
    public BaseResponse<Long> addComment(@RequestBody CommentAddRequest commentAddRequest) {
        ThrowUtils.throwIf(commentAddRequest == null, ErrorCode.PARAMS_ERROR);
        Long commentId = commentService.addComment(commentAddRequest);
        return ResultUtils.success(commentId);
    }

    /**
     * 删除评论
     *
     * @param commentDeleteRequest 评论删除请求
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    @SaCheckLogin
    public BaseResponse<Boolean> deleteComment(@RequestBody CommentDeleteRequest commentDeleteRequest) {
        ThrowUtils.throwIf(commentDeleteRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = commentService.deleteComment(commentDeleteRequest);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询评论
     *
     * @param commentQueryRequest 评论查询请求
     * @return 评论分页列表
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<CommentVO>> listCommentByPage(@RequestBody CommentQueryRequest commentQueryRequest) {
        ThrowUtils.throwIf(commentQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<CommentVO> commentVOPage = commentService.queryCommentPage(commentQueryRequest);
        return ResultUtils.success(commentVOPage);
    }

    /**
     * 获取帖子评论（两层结构）
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    @GetMapping("/post/{postId}")
    public BaseResponse<List<CommentVO>> getPostComments(@PathVariable Long postId) {
        ThrowUtils.throwIf(postId == null || postId <= 0, ErrorCode.PARAMS_ERROR);
        List<CommentVO> comments = commentService.getPostComments(postId);
        return ResultUtils.success(comments);
    }

    /**
     * 获取评论的所有回复
     *
     * @param commentId 评论ID
     * @return 回复列表
     */
    @GetMapping("/replies/{commentId}")
    public BaseResponse<List<CommentVO>> getCommentReplies(@PathVariable Long commentId) {
        ThrowUtils.throwIf(commentId == null || commentId <= 0, ErrorCode.PARAMS_ERROR);
        List<CommentVO> replies = commentService.getCommentReplies(commentId);
        return ResultUtils.success(replies);
    }

    /**
     * 点赞/取消点赞评论
     *
     * @param commentId 评论ID
     * @return 是否操作成功
     */
    @PostMapping("/like/{commentId}")
    @SaCheckLogin
    public BaseResponse<Boolean> likeComment(@PathVariable Long commentId) {
        ThrowUtils.throwIf(commentId == null || commentId <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = commentService.likeComment(commentId);
        return ResultUtils.success(result);
    }

    /**
     * 检查用户是否已点赞评论
     *
     * @param commentId 评论ID
     * @return 是否已点赞
     */
    @GetMapping("/hasLiked/{commentId}")
    @SaCheckLogin
    public BaseResponse<Boolean> hasLikedComment(@PathVariable Long commentId) {
        ThrowUtils.throwIf(commentId == null || commentId <= 0, ErrorCode.PARAMS_ERROR);
        boolean hasLiked = commentService.hasLikedComment(commentId);
        return ResultUtils.success(hasLiked);
    }

}
