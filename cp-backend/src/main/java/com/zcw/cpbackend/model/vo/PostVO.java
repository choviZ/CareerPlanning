package com.zcw.cpbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子视图对象
 *
 * @author zcw
 */
@Data
public class PostVO implements Serializable {

    /**
     * 帖子ID
     */
    private Long id;

    /**
     * 发帖用户ID
     */
    private Long userId;

    /**
     * 发帖用户信息
     */
    private UserVO user;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 是否精华：0-否，1-是
     */
    private Integer isEssence;

    /**
     * 状态：0-正常，1-待审核
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 最后评论时间
     */
    private LocalDateTime lastCommentAt;

    /**
     * 是否已点赞
     */
    private Boolean hasLiked;

    /**
     * 是否已收藏
     */
    private Boolean hasFavorited;

    private static final long serialVersionUID = 1L;
}