package com.zcw.cpbackend.model.entity.resume;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 项目经历实体类
 * 注意：此类作为JSON字段的一部分存储，不需要独立的数据库表和id字段
 *
 * @author zcw
 */
@Data
public class ProjectExperience implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 担任角色
     */
    private String role;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 项目链接
     */
    private String projectUrl;

    /**
     * 使用技术
     */
    private List<String> technologies;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目成果
     */
    private String achievements;

    /**
     * 排序
     */
    private Integer sortOrder;
}