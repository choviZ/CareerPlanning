package com.zcw.cpbackend.model.dto.post;

import com.zcw.cpbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 帖子查询请求
 *
 * @author zcw
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostQueryRequest extends PageRequest implements Serializable {

    /**
     * 帖子标题（模糊查询）
     */
    private String title;

    /**
     * 帖子内容（模糊查询）
     */
    private String content;

    /**
     * 标签
     */
    private String tags;

    /**
     * 发帖用户ID
     */
    private Long userId;

    /**
     * 是否精华：0-否，1-是
     */
    private Integer isEssence;

    /**
     * 状态：0-正常，1-待审核
     */
    private Integer status;

    /**
     * 排序字段：created_at, view_count, like_count, comment_count, last_comment_at
     */
    private String sortField;

    /**
     * 排序方向：asc, desc
     */
    private String sortOrder;

    private static final long serialVersionUID = 1L;
}