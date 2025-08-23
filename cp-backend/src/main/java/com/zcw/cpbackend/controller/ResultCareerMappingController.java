package com.zcw.cpbackend.controller;

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
@RequestMapping("/resultCareerMapping")
public class ResultCareerMappingController {

    @Resource
    private ResultCareerMappingService resultCareerMappingService;


}
