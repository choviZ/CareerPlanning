package com.zcw.cpbackend.model.entity;

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
 * 测评题目表 实体类。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("assessment_question")
public class AssessmentQuestion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 测评类型
     */
    private String testType;

    /**
     * 题目内容，如“你更喜欢独处还是社交？”
     */
    private String content;

    /**
     * 维度标识，如 MBTI 的 E/I, S/N, T/F, J/P；霍兰德的 R/I/A/S/E/C
     */
    private String dimension;

    /**
     * 选项数组，包含内容和分数规则
     */
    private String options;

    /**
     * 题目排序序号
     */
    private Integer sortOrder;

    /**
     * 状态：1-启用，0-禁用
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

}
