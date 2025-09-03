package com.zcw.cpbackend.service;

import com.zcw.cpbackend.model.dto.analyse.ResourceTypeDistributionDTO;

import java.util.List;

public interface AnalyseService {

    /**
     * 计算资源类型分布
     *
     * @return
     */
    List<ResourceTypeDistributionDTO> calculateTypeDistribution();
}
