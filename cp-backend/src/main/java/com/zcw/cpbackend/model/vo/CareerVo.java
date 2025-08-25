package com.zcw.cpbackend.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.zcw.cpbackend.model.entity.Career;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 职业表Career Vo
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
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

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 是否删除
     */
    private Boolean isDeleted;


    public static CareerVo objToVo(Career career){
        if (career == null) {
            return null;
        }
        CareerVo careerVo = new CareerVo();
        BeanUtil.copyProperties(career, careerVo);
        return careerVo;
    }
}
