package com.zcw.cpbackend.model.dto.career;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新职业请求
 *
 * @author zcw
 */
@Data
public class CareerUpdateRequest implements Serializable {

    /**
     * 职业ID
     */
    private Long id;

    /**
     * 职业名称
     */
    private String name;

    /**
     * 职业描述
     */
    private String description;

    /**
     * 所需技能
     */
    private String requiredSkills;

    /**
     * 就业前景
     */
    private String jobOutlook;

    /**
     * 平均薪资范围
     */
    private String averageSalary;

}