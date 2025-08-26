package com.zcw.cpbackend.model.dto.resumetemplate;

import com.zcw.cpbackend.model.dto.resume.ResumeContent;
import lombok.Data;

import java.io.Serializable;

/**
 * 简历模板添加请求
 *
 * @author zcw
 */
@Data
public class ResumeTemplateAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板描述
     */
    private String templateDesc;

    /**
     * 模板预览图URL
     */
    private String previewUrl;

    /**
     * 模板配置（JSON格式）
     */
    private Object templateConfig;

    /**
     * 默认内容（JSON格式）
     */
    private ResumeContent defaultContent;

    /**
     * 模板类型：1-基础模板，2-高级模板，3-专业模板
     */
    private Integer templateType;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer isActive;
}