package com.zcw.cpbackend.model.dto.resume;

import com.zcw.cpbackend.model.entity.resume.ResumeContent;
import lombok.Data;

import java.io.Serializable;

/**
 * 简历添加请求
 *
 * @author zcw
 */
@Data
public class ResumeAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 简历标题
     */
    private String title;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 简历内容
     */
    private ResumeContent content;

    /**
     * 状态：1-草稿，2-已完成，3-已发布
     */
    private Integer status;

    /**
     * 是否公开：0-私有，1-公开
     */
    private Integer isPublic;
}