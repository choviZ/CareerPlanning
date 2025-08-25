package com.zcw.cpbackend.model.dto.resume;

import com.zcw.cpbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 简历查询请求
 *
 * @author zcw
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 简历标题
     */
    private String title;

    /**
     * 姓名
     */
    private String name;

    /**
     * 求职意向
     */
    private String jobIntention;

    /**
     * 状态：1-草稿，2-已完成，3-已发布
     */
    private Integer status;

    /**
     * 是否公开：0-私有，1-公开
     */
    private Integer isPublic;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 用户ID（管理员查询时使用）
     */
    private Long userId;
}