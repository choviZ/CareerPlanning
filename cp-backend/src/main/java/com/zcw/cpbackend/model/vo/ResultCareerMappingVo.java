package com.zcw.cpbackend.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.zcw.cpbackend.model.entity.ResultCareerMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 测评结果职业关联表 实体类。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultCareerMappingVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 测评结果ID
     */
    private Long resultId;

    /**
     * 测评类型
     */
    private String testType;

    /**
     * 测评结果代码
     */
    private String resultCode;

    /**
     * 职业ID
     */
    private Long careerId;

    /**
     * 职业名称
     */
    private String careerName;

    /**
     * 职业描述
     */
    private String description;

    /**
     * 兼容性评分（0-100）
     */
    private Integer compatibilityScore;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 是否删除
     */
    private int isDeleted;

    public static ResultCareerMapping voToObj(ResultCareerMappingVo vo) {
        if (vo == null) {
            return null;
        }
        ResultCareerMapping resultCareerMapping = new ResultCareerMapping();
        BeanUtil.copyProperties(vo, resultCareerMapping);
        return resultCareerMapping;
    }

    public static ResultCareerMappingVo objToVo(ResultCareerMapping obj) {
        if (obj == null) {
            return null;
        }
        ResultCareerMappingVo resultCareerMappingVo = new ResultCareerMappingVo();
        BeanUtil.copyProperties(obj, resultCareerMappingVo);
        return resultCareerMappingVo;
    }
}
