package com.zcw.cpbackend.model.vo;

import com.zcw.cpbackend.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论视图对象
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 所属帖子ID
     */
    private Long postId;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 评论用户信息
     */
    private UserVO user;

    /**
     * 父评论ID，NULL表示顶级评论
     */
    private Long parentId;

    /**
     * 根评论ID，用于快速查找评论树
     */
    private Long rootId;

    /**
     * 回复的用户ID
     */
    private Long replyToUserId;

    /**
     * 回复的用户信息
     */
    private UserVO replyToUser;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 回复数
     */
    private Integer replyCount;

    /**
     * 评论层级：1-顶级评论，2-二级评论，以此类推
     */
    private Integer level;

    /**
     * 评论路径，如：1/23/456，便于查询评论树
     */
    private String path;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 是否已点赞
     */
    private Boolean hasLiked;

    /**
     * 子评论列表（仅用于二级评论展示）
     */
    private List<CommentVO> children;

    /**
     * 对象转包装类
     *
     * @param comment 原始对象
     * @return 包装类
     */
    public static CommentVO objToVo(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        return commentVO;
    }

    /**
     * 包装类转对象
     *
     * @param commentVO 包装类
     * @return 对象
     */
    public static Comment voToObj(CommentVO commentVO) {
        if (commentVO == null) {
            return null;
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentVO, comment);
        return comment;
    }
}