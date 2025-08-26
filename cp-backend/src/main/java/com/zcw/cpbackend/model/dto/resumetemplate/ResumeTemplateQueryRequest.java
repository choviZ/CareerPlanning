package com.zcw.cpbackend.model.dto.resumetemplate;

import com.zcw.cpbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 简历模板查询请求
 *
 * @author zcw
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeTemplateQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型：1-基础模板，2-高级模板，3-专业模板
     */
    private Integer templateType;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer isActive;

}