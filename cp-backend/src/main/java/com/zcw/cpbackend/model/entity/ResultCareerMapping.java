package com.zcw.cpbackend.model.entity;

import com.mybatisflex.annotation.Column;
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
 * 测评结果职业关联表 实体类。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("result_career_mapping")
public class ResultCareerMapping implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 测评结果ID
     */
    private Long resultId;

    /**
     * 测评类型
     */
    private String testType;

    /**
     * 测评结果代码
     */
    private String resultCode;

    /**
     * 职业ID
     */
    private Long careerId;

    /**
     * 职业名称
     */
    private String careerName;

    /**
     * 职业描述
     */
    private String description;

    /**
     * 兼容性评分（0-100）
     */
    private Integer compatibilityScore;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 是否删除
     */
    @Column(isLogicDelete = true)
    private int isDeleted;
}
