package com.zcw.cpbackend.model.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 题目请求（添加/修改）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddQuestionRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 题目ID（修改时使用）
     */
    private Long id;

    /**
     * 评估类型
     */
    private String testType;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 维度标识
     */
    private String dimension;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 选项数组，包含内容和分数规则
     */
    private OptionDTO options;

    /**
     * 题目排序序号
     */
    private Integer sortOrder;
}
