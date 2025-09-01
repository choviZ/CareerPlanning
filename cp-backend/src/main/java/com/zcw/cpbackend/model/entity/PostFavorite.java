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
 * 用户收藏表实体类
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("post_favorite")
public class PostFavorite {

    /**
     * 收藏ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}