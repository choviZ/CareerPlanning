package com.zcw.cpbackend.model.dto.comment;

import com.zcw.cpbackend.common.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 评论查询请求
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 所属帖子ID
     */
    private Long postId;

    /**
     * 父评论ID，NULL表示查询顶级评论
     */
    private Long parentId;

    /**
     * 根评论ID，用于查询某个评论树下的所有评论
     */
    private Long rootId;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 评论内容（模糊查询）
     */
    private String content;

    /**
     * 评论层级：1-顶级评论，2-二级评论
     */
    private Integer level;
}