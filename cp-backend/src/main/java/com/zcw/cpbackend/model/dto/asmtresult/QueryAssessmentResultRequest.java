package com.zcw.cpbackend.model.dto.asmtresult;

import com.zcw.cpbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询测评结果请求
 *
 * @author zcw
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryAssessmentResultRequest extends PageRequest implements Serializable {

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