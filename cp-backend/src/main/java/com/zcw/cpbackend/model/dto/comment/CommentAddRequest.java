package com.zcw.cpbackend.model.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 评论添加请求
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 所属帖子ID
     */
    private Long postId;

    /**
     * 父评论ID，NULL表示顶级评论
     */
    private Long parentId;

    /**
     * 回复的用户ID（当回复某个用户的评论时）
     */
    private Long replyToUserId;

    /**
     * 评论内容
     */
    private String content;
}