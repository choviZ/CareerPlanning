package com.zcw.cpbackend.model.dto.learningresource;

import com.zcw.cpbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 学习资源查询请求
 *
 * @author zcw
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LearningResourceQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源摘要
     */
    private String summary;

    /**
     * 资源类型：1-技术文档，2-行业动态
     */
    private Integer resourceType;

    /**
     * 内容类型：1-markdown
     */
    private Integer contentType;

    /**
     * 分类名称
     */
    private String category;

    /**
     * 发布者ID
     */
    private Long authorId;

    /**
     * 是否置顶：0-否，1-是
     */
    private Integer isTop;

    /**
     * 关键词搜索（标题、摘要、内容）
     */
    private String keyword;

}