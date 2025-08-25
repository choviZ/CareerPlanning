package com.zcw.cpbackend.model.entity.resume;

import lombok.Data;

import java.io.Serializable;

/**
 * 技能实体类
 *
 * @author zcw
 */
@Data
public class Skill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 技能名称
     */
    private String skillName;

    /**
     * 技能等级：1-初级，2-中级，3-高级，4-专家
     */
    private Integer skillLevel;

    /**
     * 技能分类
     */
    private String category;

    /**
     * 排序
     */
    private Integer sortOrder;
}