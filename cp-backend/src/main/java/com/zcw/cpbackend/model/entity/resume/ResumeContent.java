package com.zcw.cpbackend.model.entity.resume;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 简历内容聚合类
 * 用于JSON存储的完整简历内容
 *
 * @author zcw
 */
@Data
public class ResumeContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 基本信息
     */
    private BasicInfo basicInfo;

    /**
     * 教育经历列表
     */
    private List<Education> education;

    /**
     * 工作经历列表
     */
    private List<WorkExperience> workExperience;

    /**
     * 项目经历列表
     */
    private List<ProjectExperience> projectExperience;

    /**
     * 技能列表
     */
    private List<Skill> skills;
}