package com.zcw.cpbackend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体类。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("learning_resource")
public class LearningResource implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 文本内容
     */
    private String content;

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
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 发布者ID
     */
    private Long authorId;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 是否置顶：0-否，1-是
     */
    private Integer isTop;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 是否删除：0-否，1-是
     */
    @Column(isLogicDelete = true)
    private Integer isDeleted;

}
