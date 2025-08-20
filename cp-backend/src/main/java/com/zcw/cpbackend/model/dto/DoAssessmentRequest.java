package com.zcw.cpbackend.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 获取测评结果请求
 */
@Data
public class DoAssessmentRequest {

    private String testType;

    private List<String> userAnswers;
}
