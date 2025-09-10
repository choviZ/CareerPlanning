package com.zcw.cpbackend.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.zcw.cpbackend.model.entity.ResumeTemplate;
import com.zcw.cpbackend.model.dto.resume.ResumeContent;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 简历模板视图对象
 *
 * @author zcw
 */
@Data
public class ResumeTemplateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

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


    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 对象转换
     *
     * @param resumeTemplate 简历模板实体
     * @return 简历模板VO
     */
    public static ResumeTemplateVo objToVo(ResumeTemplate resumeTemplate) {
        if (resumeTemplate == null) {
            return null;
        }
        ResumeTemplateVo resumeTemplateVo = new ResumeTemplateVo();
        BeanUtil.copyProperties(resumeTemplate, resumeTemplateVo);
        return resumeTemplateVo;
    }
}