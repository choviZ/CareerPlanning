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
 * 测评结果表 实体类。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("assessment_result")
public class AssessmentResult implements Serializable {

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
     * 测评类型
     */
    private String testType;

    /**
     * 结果代码（如INTJ、RIA等）
     */
    private String resultCode;

    /**
     * 各维度得分详情
     */
    private String dimensionScores;

    /**
     * 完成时间
     */
    private LocalDateTime createdAt;

    /**
     * 是否删除
     */
    @Column(isLogicDelete = true)
    private int isDeleted;

}
