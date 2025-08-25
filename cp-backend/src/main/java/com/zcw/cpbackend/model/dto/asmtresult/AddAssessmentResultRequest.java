package com.zcw.cpbackend.model.dto.asmtresult;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加测评结果请求
 *
 * @author zcw
 */
@Data
public class AddAssessmentResultRequest implements Serializable {

    /**
     * 测评类型
     */
    private String testType;

    /**
     * 结果代码（如INTJ、RIA等）
     */
    private String resultCode;

    /**
     * 测评结果名称
     */
    private String resultName;

    /**
     * 测评结果描述
     */
    private String resultDesc;

}