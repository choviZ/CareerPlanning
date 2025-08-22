package com.zcw.cpbackend.model.dto.assessment;

import lombok.Data;

/**
 * 题目选项
 */
@Data
public class OptionDTO {

    /**
     * 选项的key
     */
    private String key;

    /**
     * 选项的value
     */
    private String value;

    /**
     * 维度
     */
    private String dimension;
}
