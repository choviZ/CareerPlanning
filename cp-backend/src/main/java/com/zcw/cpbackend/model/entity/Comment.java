package com.zcw.cpbackend.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论表 实体类。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("comment")
public class Comment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @Id(keyType = KeyType.Auto)
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
     * 是否删除（0-否，1-是）
     */
    private Integer isDeleted;

}
