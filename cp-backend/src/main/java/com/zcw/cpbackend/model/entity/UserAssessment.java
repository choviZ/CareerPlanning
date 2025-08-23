package com.zcw.cpbackend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;
import java.util.List;

import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户评估记录表 实体类。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user_assessment")
public class UserAssessment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Generator,value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 评估类型
     */
    private String testType;

    /**
     * 结果代码
     */
    private String resultCode;

    /**
     * 测评结果名称
     */
    private String resultName;

    /**
     * 测评结果描述
     */
    private String resultDesc;

    /**
     * 图标
     */
    private String resultIcon;

    /**
     * 用户选择的选项列表
     */
    private String choices;

    /**
     * 各维度的得分-Json
     */
    private String dimensionScores;

    /**
     * 用户ID
     */
    private Long userId;

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
    private String careerDescription;

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
