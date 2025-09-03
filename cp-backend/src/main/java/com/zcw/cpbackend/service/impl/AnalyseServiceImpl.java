package com.zcw.cpbackend.service.impl;

import com.zcw.cpbackend.model.dto.analyse.ResourceTypeDistributionDTO;
import com.zcw.cpbackend.mapper.AnalyseMapper;
import com.zcw.cpbackend.service.AnalyseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyseServiceImpl implements AnalyseService {

    @Resource
    private AnalyseMapper analyseMapper;

    @Override
    public List<ResourceTypeDistributionDTO> calculateTypeDistribution() {
        return analyseMapper.calculateTypeDistribution();
    }
}
