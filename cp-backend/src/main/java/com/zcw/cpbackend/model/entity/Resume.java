package com.zcw.cpbackend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.zcw.cpbackend.model.dto.resume.ResumeContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 简历表 实体类。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("resume")
public class Resume implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Generator,value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 简历标题
     */
    private String title;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 简历内容（JSON格式）
     */
    @Column(typeHandler = com.zcw.cpbackend.config.CustomJacksonTypeHandler.class)
    private ResumeContent content;

    /**
     * 姓名（冗余字段，便于查询）
     */
    private String name;

    /**
     * 求职意向（冗余字段，便于查询）
     */
    private String jobIntention;

    /**
     * 状态：1-草稿，2-已完成，3-已发布
     */
    private Integer status;

    /**
     * 分享码
     */
    private String shareCode;

    /**
     * 是否公开：0-私有，1-公开
     */
    private Integer isPublic;

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