package com.zcw.cpbackend.model.entity.resume;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 教育经历实体类
 *
 * @author zcw
 */
@Data
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 专业
     */
    private String major;

    /**
     * 学历
     */
    private String degree;

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
     * 是否在读
     */
    private Boolean isCurrent;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer sortOrder;
}