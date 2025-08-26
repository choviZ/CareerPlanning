package com.zcw.cpbackend.model.dto.resume;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 技能实体类
 * 注意：此类作为JSON字段的一部分存储，不需要独立的数据库表和id字段
 *
 * @author zcw
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 便于创建技能对象的构造函数
     * @param skillName 技能名称
     * @param skillLevel 技能等级
     */
    public Skill(String skillName, Integer skillLevel) {
        this.skillName = skillName;
        this.skillLevel = skillLevel;
    }
}