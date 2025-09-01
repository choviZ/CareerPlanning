package com.zcw.cpbackend.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户点赞表实体类
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("post_like")
public class PostLike {

    /**
     * 点赞ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 点赞目标类型：1-帖子，2-评论
     */
    private Integer targetType;

    /**
     * 目标ID（帖子ID或评论ID）
     */
    private Long targetId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}