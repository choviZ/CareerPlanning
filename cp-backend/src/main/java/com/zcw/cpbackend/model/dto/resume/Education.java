package com.zcw.cpbackend.model.dto.resume;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 教育经历实体类
 * 注意：此类作为JSON字段的一部分存储，不需要独立的数据库表和id字段
 *
 * @author zcw
 */
@Data
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

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