package com.zcw.cpbackend.model.entity.resume;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 工作经历实体类
 * 注意：此类作为JSON字段的一部分存储，不需要独立的数据库表和id字段
 *
 * @author zcw
 */
@Data
public class WorkExperience implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 职位
     */
    private String position;

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
     * 是否在职
     */
    private Boolean isCurrent;

    /**
     * 工作描述
     */
    private String jobDescription;

    /**
     * 工作成就
     */
    private String achievements;

    /**
     * 排序
     */
    private Integer sortOrder;
}