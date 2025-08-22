package com.zcw.cpbackend.model.dto.career;

import com.zcw.cpbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询职业请求
 *
 * @author zcw
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CareerQueryRequest extends PageRequest implements Serializable {

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