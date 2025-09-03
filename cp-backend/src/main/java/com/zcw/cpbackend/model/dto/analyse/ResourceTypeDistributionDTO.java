package com.zcw.cpbackend.model.dto.analyse;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 资源类型分布
 */
@Data
public class ResourceTypeDistributionDTO {
    /**
     * 资源类型（1-技术文档，2-行业动态）
     */
    private Integer resourceType;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 该类型资源数
     */
    private Long typeCount;

    /**
     * 该类型资源占比
     */
    private BigDecimal typeRatio;

    /**
     * 该类型资源平均浏览量
     */
    private BigDecimal avgView;

    /**
     * 该类型资源总浏览量
     */
    private Long totalView;

    /**
     * 该类型资源置顶数量
     */
    private Long topCount;
}