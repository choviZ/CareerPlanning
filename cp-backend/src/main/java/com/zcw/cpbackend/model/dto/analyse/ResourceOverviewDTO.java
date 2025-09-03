package com.zcw.cpbackend.model.dto.analyse;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 资源概览数据
 */
@Data
public class ResourceOverviewDTO {
    private Long totalResources;         // 总资源数
    private Long validResources;         // 有效资源数
    private Long deletedResources;       // 已删除资源数
    private BigDecimal avgViewCount;     // 平均浏览量
    private Long totalViewCount;         // 总浏览量
}