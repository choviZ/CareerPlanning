package com.zcw.cpbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.BusinessException;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.exception.ThrowUtils;
import com.zcw.cpbackend.model.dto.comment.CommentAddRequest;
import com.zcw.cpbackend.model.dto.comment.CommentDeleteRequest;
import com.zcw.cpbackend.model.dto.comment.CommentQueryRequest;
import com.zcw.cpbackend.model.entity.Comment;
import com.zcw.cpbackend.model.entity.Post;
import com.zcw.cpbackend.model.entity.User;
import com.zcw.cpbackend.model.vo.CommentVO;
import com.zcw.cpbackend.model.vo.UserVO;
import com.zcw.cpbackend.mapper.CommentMapper;
import com.zcw.cpbackend.service.CommentService;
import com.zcw.cpbackend.service.PostLikeService;
import com.zcw.cpbackend.service.PostService;
import com.zcw.cpbackend.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 评论表 服务层实现。
 *
 * @author zcw
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Resource
    private PostLikeService postLikeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addComment(CommentAddRequest commentAddRequest) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 参数校验
        ThrowUtils.throwIf(commentAddRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(commentAddRequest.getPostId() == null || commentAddRequest.getPostId() <= 0, ErrorCode.PARAMS_ERROR, "帖子ID不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(commentAddRequest.getContent()), ErrorCode.PARAMS_ERROR, "评论内容不能为空");
        ThrowUtils.throwIf(commentAddRequest.getContent().length() > 1000, ErrorCode.PARAMS_ERROR, "评论内容过长");
        // 检查帖子是否存在
        Post post = postService.getById(commentAddRequest.getPostId());
        ThrowUtils.throwIf(post == null, ErrorCode.NOT_FOUND_ERROR, "帖子不存在");
        ThrowUtils.throwIf(post.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "帖子已删除");
        // 构建评论对象
        Comment comment = Comment.builder()
                .postId(commentAddRequest.getPostId())
                .userId(userId)
                .content(commentAddRequest.getContent())
                .likeCount(0)
                .replyCount(0)
                .createdAt(LocalDateTime.now())
                .isDeleted(0)
                .build();
        // 处理层级关系
        if (commentAddRequest.getParentId() != null && commentAddRequest.getParentId() > 0) {
            // 回复评论
            Comment parentComment = this.getById(commentAddRequest.getParentId());
            ThrowUtils.throwIf(parentComment == null, ErrorCode.NOT_FOUND_ERROR, "父评论不存在");
            ThrowUtils.throwIf(parentComment.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "父评论已删除");
            ThrowUtils.throwIf(!Objects.equals(parentComment.getPostId(), commentAddRequest.getPostId()), ErrorCode.PARAMS_ERROR, "父评论与帖子不匹配");

            comment.setParentId(commentAddRequest.getParentId());
            comment.setReplyToUserId(commentAddRequest.getReplyToUserId());

            // 设置根评论ID和层级
            if (parentComment.getLevel() == 1) {
                // 回复顶级评论
                comment.setRootId(parentComment.getId());
                comment.setLevel(2);
                comment.setPath(parentComment.getPath() + "/" + parentComment.getId());
            } else {
                // 回复二级评论，统一归到二级
                comment.setRootId(parentComment.getRootId());
                comment.setLevel(2);
                comment.setPath(parentComment.getPath());
            }
        } else {
            // 顶级评论
            comment.setLevel(1);
        }
        // 保存评论
        boolean saved = this.save(comment);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "评论发布失败");
        // 更新路径（顶级评论）
        if (comment.getLevel() == 1) {
            comment.setPath(String.valueOf(comment.getId()));
            this.updateById(comment);
        }
        // 更新父评论回复数
        if (comment.getParentId() != null) {
            this.incrementReplyCount(comment.getParentId());
        }
        // 更新帖子评论数
        postService.updatePostStats(comment.getPostId(), null, 1);
        log.info("用户 {} 在帖子 {} 发布了评论 {}", userId, comment.getPostId(), comment.getId());
        return comment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteComment(CommentDeleteRequest commentDeleteRequest) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 参数校验
        ThrowUtils.throwIf(commentDeleteRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(commentDeleteRequest.getId() == null || commentDeleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR, "评论ID不能为空");
        ThrowUtils.throwIf(userId <= 0, ErrorCode.NOT_LOGIN_ERROR);
        Long commentId = commentDeleteRequest.getId();
        // 查询评论
        Comment comment = this.getById(commentId);
        ThrowUtils.throwIf(comment == null, ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        ThrowUtils.throwIf(comment.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "评论已删除");
        // 权限检查（只能删除自己的评论）
        ThrowUtils.throwIf(!Objects.equals(comment.getUserId(), userId), ErrorCode.NO_AUTH_ERROR, "无权限删除此评论");
        // 逻辑删除
        comment.setIsDeleted(1);
        boolean updated = this.updateById(comment);
        ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "评论删除失败");
        // 更新父评论回复数
        if (comment.getParentId() != null) {
            this.decrementReplyCount(comment.getParentId());
        }
        // 更新帖子评论数
        postService.updatePostStats(comment.getPostId(), null, -1);
        log.info("用户 {} 删除了评论 {}", userId, commentId);
        return true;
    }

    @Override
    public Page<CommentVO> queryCommentPage(CommentQueryRequest commentQueryRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        // 参数校验
        ThrowUtils.throwIf(commentQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 构建查询条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(Comment::getIsDeleted, 0);
        // 添加查询条件
        if (commentQueryRequest.getPostId() != null) {
            queryWrapper.eq(Comment::getPostId, commentQueryRequest.getPostId());
        }
        if (commentQueryRequest.getParentId() != null) {
            queryWrapper.eq(Comment::getParentId, commentQueryRequest.getParentId());
        }
        if (commentQueryRequest.getRootId() != null) {
            queryWrapper.eq(Comment::getRootId, commentQueryRequest.getRootId());
        }
        if (commentQueryRequest.getUserId() != null) {
            queryWrapper.eq(Comment::getUserId, commentQueryRequest.getUserId());
        }
        if (StrUtil.isNotBlank(commentQueryRequest.getContent())) {
            queryWrapper.like(Comment::getContent, commentQueryRequest.getContent());
        }
        if (commentQueryRequest.getLevel() != null) {
            queryWrapper.eq(Comment::getLevel, commentQueryRequest.getLevel());
        }
        // 排序：按创建时间倒序
        queryWrapper.orderBy(Comment::getCreatedAt, false);
        // 分页查询
        Page<Comment> commentPage = this.page(Page.of(commentQueryRequest.getCurrent(), commentQueryRequest.getPageSize()), queryWrapper);
        // 转换为VO
        List<CommentVO> commentVOList = commentPage.getRecords().stream()
                .map(comment -> buildCommentVO(comment, currentUserId))
                .collect(Collectors.toList());
        // 构建分页结果
        Page<CommentVO> commentVOPage = new Page<>();
        commentVOPage.setRecords(commentVOList);
        commentVOPage.setTotalRow(commentPage.getTotalRow());
        commentVOPage.setPageNumber(commentPage.getPageNumber());
        commentVOPage.setPageSize(commentPage.getPageSize());
        return commentVOPage;
    }

    @Override
    public List<CommentVO> getPostComments(Long postId) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        // 参数校验
        ThrowUtils.throwIf(postId == null || postId <= 0, ErrorCode.PARAMS_ERROR, "帖子ID不能为空");
        // 查询顶级评论
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(Comment::getPostId, postId)
                .eq(Comment::getLevel, 1)
                .eq(Comment::getIsDeleted, 0)
                .orderBy(Comment::getCreatedAt, false);
        List<Comment> topComments = this.list(queryWrapper);
        // 转换为VO并加载子评论
        return topComments.stream()
                .map(comment -> {
                    CommentVO commentVO = buildCommentVO(comment, currentUserId);
                    // 加载二级回复（最多显示3条）
                    List<CommentVO> children = getTopReplies(comment.getId(), 3, currentUserId);
                    commentVO.setChildren(children);
                    return commentVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentVO> getCommentReplies(Long commentId) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        // 参数校验
        ThrowUtils.throwIf(commentId == null || commentId <= 0, ErrorCode.PARAMS_ERROR, "评论ID不能为空");
        // 查询回复
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(Comment::getRootId, commentId)
                .eq(Comment::getIsDeleted, 0)
                .orderBy(Comment::getCreatedAt, true); // 回复按时间正序
        List<Comment> replies = this.list(queryWrapper);
        // 转换为VO
        return replies.stream()
                .map(comment -> buildCommentVO(comment, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean likeComment(Long commentId) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 参数校验
        ThrowUtils.throwIf(commentId == null || commentId <= 0, ErrorCode.PARAMS_ERROR, "评论ID不能为空");
        ThrowUtils.throwIf(userId <= 0, ErrorCode.NOT_LOGIN_ERROR);
        // 检查评论是否存在
        Comment comment = this.getById(commentId);
        ThrowUtils.throwIf(comment == null, ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        ThrowUtils.throwIf(comment.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "评论已删除");
        // 调用点赞服务（targetType=2表示评论）
        Boolean result = postLikeService.toggleLike(userId, 2, commentId);
        // 更新评论点赞数
        int likeChange = result ? 1 : -1;
        comment.setLikeCount(comment.getLikeCount() + likeChange);
        this.updateById(comment);
        return result;
    }

    @Override
    public Boolean hasLikedComment(Long commentId) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            if (userId <= 0 || commentId == null || commentId <= 0) {
                return false;
            }
            return postLikeService.hasLiked(userId, 2, commentId);
        } catch (Exception e) {
            // 用户未登录
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean incrementReplyCount(Long commentId) {
        ThrowUtils.throwIf(commentId == null || commentId <= 0, ErrorCode.PARAMS_ERROR, "评论ID不能为空");
        Comment comment = this.getById(commentId);
        ThrowUtils.throwIf(comment == null, ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        comment.setReplyCount(comment.getReplyCount() + 1);
        return this.updateById(comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean decrementReplyCount(Long commentId) {
        ThrowUtils.throwIf(commentId == null || commentId <= 0, ErrorCode.PARAMS_ERROR, "评论ID不能为空");
        Comment comment = this.getById(commentId);
        ThrowUtils.throwIf(comment == null, ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        comment.setReplyCount(Math.max(0, comment.getReplyCount() - 1));
        return this.updateById(comment);
    }

    /**
     * 构建评论VO对象
     */
    private CommentVO buildCommentVO(Comment comment, Long currentUserId) {
        CommentVO commentVO = CommentVO.objToVo(comment);
        // 设置用户信息
        UserVO userVO = userService.queryUserById(comment.getUserId());
        commentVO.setUser(userVO);
        // 设置回复用户信息
        if (comment.getReplyToUserId() != null) {
            UserVO replyToUserVO = userService.queryUserById(comment.getReplyToUserId());
            commentVO.setReplyToUser(replyToUserVO);
        }
        // 设置是否已点赞
        if (currentUserId != null) {
            Boolean hasLiked = hasLikedComment(comment.getId());
            commentVO.setHasLiked(hasLiked);
        } else {
            commentVO.setHasLiked(false);
        }
        return commentVO;
    }

    /**
     * 获取顶级回复（用于首页展示）
     */
    private List<CommentVO> getTopReplies(Long rootId, int limit, Long currentUserId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(Comment::getRootId, rootId)
                .eq(Comment::getIsDeleted, 0)
                .orderBy(Comment::getCreatedAt, true)
                .limit(limit);
        List<Comment> replies = this.list(queryWrapper);
        return replies.stream()
                .map(comment -> buildCommentVO(comment, currentUserId))
                .collect(Collectors.toList());
    }
}
