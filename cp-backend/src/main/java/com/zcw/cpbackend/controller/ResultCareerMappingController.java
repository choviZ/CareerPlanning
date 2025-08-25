package com.zcw.cpbackend.controller;

import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.model.vo.ResultCareerMappingVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zcw.cpbackend.service.ResultCareerMappingService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测评结果职业关联表 控制层。
 *
 * @author zcw
 */
@RestController
@RequestMapping("/result-career")
public class ResultCareerMappingController {

    @Resource
    private ResultCareerMappingService resultCareerMappingService;

    

}
