package com.zcw.cpbackend.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.zcw.cpbackend.model.entity.AssessmentResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 测评结果Vo。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 测评类型
     */
    private String testType;

    /**
     * 结果代码（如INTJ、RIA等）
     */
    private String resultCode;

    /**
     * 测评结果名称
     */
    private String resultName;

    /**
     * 测评结果描述
     */
    private String resultDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 是否删除（0-否，1-是）
     */
    private Integer isDeleted;

    /**
     * 对象转包装类
     *
     * @param assessmentResult 原始对象
     * @return 包装类
     */
    public static AssessmentResultVo objToVo(AssessmentResult assessmentResult) {
        if (assessmentResult == null) {
            return null;
        }
        AssessmentResultVo assessmentResultVo = new AssessmentResultVo();
        BeanUtil.copyProperties(assessmentResult, assessmentResultVo);
        return assessmentResultVo;
    }

}
