package com.zcw.cpbackend.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.zcw.cpbackend.model.entity.Resume;
import com.zcw.cpbackend.model.entity.resume.ResumeContent;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 简历视图对象
 *
 * @author zcw
 */
@Data
public class ResumeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

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
     * 分享码
     */
    private String shareCode;

    /**
     * 是否公开：0-私有，1-公开
     */
    private Integer isPublic;

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
     * @param resume 简历实体
     * @return 简历VO
     */
    public static ResumeVo objToVo(Resume resume) {
        if (resume == null) {
            return null;
        }
        ResumeVo resumeVo = new ResumeVo();
        BeanUtil.copyProperties(resume, resumeVo);
        return resumeVo;
    }
}