package com.zcw.cpbackend.controller;

import com.zcw.cpbackend.mapper.AnalyseMapper;
import com.zcw.cpbackend.model.dto.analyse.ResourceOverviewDTO;
import com.zcw.cpbackend.service.AnalyseService;
import com.zcw.cpbackend.model.dto.analyse.ResourceTypeDistributionDTO;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计分析接口
 */
@RestController
@RequestMapping("/analyse")
public class AnalyseController {

    @Resource
    private AnalyseService analyseService;
    @Resource
    private AnalyseMapper analyseMapper;

    @GetMapping("/typeDistribution")
    public BaseResponse<List<ResourceTypeDistributionDTO>> getTypeDistribution(){
        List<ResourceTypeDistributionDTO> yypeDistributionList = analyseService.calculateTypeDistribution();
        return ResultUtils.success(yypeDistributionList);
    }

    @GetMapping("/resourceOverview")
    public BaseResponse<ResourceOverviewDTO> getResourceOverview(){
        ResourceOverviewDTO resourceOverview = analyseMapper.getResourceOverview();
        return ResultUtils.success(resourceOverview);
    }
}
