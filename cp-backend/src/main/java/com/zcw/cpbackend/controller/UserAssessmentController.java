package com.zcw.cpbackend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zcw.cpbackend.service.UserAssessmentService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户评估记录表 控制层。
 *
 * @author zcw
 */
@RestController
@RequestMapping("/userAssessment")
public class UserAssessmentController {

    @Resource
    private UserAssessmentService userAssessmentService;

}
