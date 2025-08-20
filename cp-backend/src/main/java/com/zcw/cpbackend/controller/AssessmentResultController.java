package com.zcw.cpbackend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zcw.cpbackend.service.AssessmentResultService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测评结果表 控制层。
 *
 * @author zcw
 */
@RestController
@RequestMapping("/assessmentResult")
public class AssessmentResultController {

    @Resource
    private AssessmentResultService assessmentResultService;

}
