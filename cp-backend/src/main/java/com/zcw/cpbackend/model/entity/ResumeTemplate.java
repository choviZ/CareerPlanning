package com.zcw.cpbackend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.zcw.cpbackend.model.entity.resume.ResumeContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 简历模板表 实体类。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("resume_template")
public class ResumeTemplate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板描述
     */
    private String templateDesc;

    /**
     * 模板预览图URL
     */
    private String previewUrl;

    /**
     * 模板配置（JSON格式）
     */
    @Column(typeHandler = com.zcw.cpbackend.config.GenericJacksonTypeHandler.class)
    private Object templateConfig;

    /**
     * 默认内容（JSON格式）
     */
    @Column(typeHandler = com.zcw.cpbackend.config.CustomJacksonTypeHandler.class)
    private ResumeContent defaultContent;

    /**
     * 模板类型：1-基础模板，2-高级模板，3-专业模板
     */
    private Integer templateType;

    /**
     * 是否免费：0-付费，1-免费
     */
    private Integer isFree;

    /**
     * 价格（分）
     */
    private Integer price;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 使用次数
     */
    private Integer useCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除标记：0-未删除，1-已删除
     */
    @Column(isLogicDelete = true)
    private Integer isDeleted;
}