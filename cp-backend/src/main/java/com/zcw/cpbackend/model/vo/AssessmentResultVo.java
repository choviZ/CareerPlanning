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
 * 测评结果表 实体类。
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
     * 用户ID
     */
    private Long userId;

    /**
     * 测评类型
     */
    private String testType;

    /**
     * 结果代码（如INTJ、RIA等）
     */
    private String resultCode;

    /**
     * 各维度得分详情
     */
    private String dimensionScores;

    /**
     * 完成时间
     */
    private LocalDateTime createdAt;

    /**
     * 是否删除
     */
    private int isDeleted;


    /**
     * 对象转包装类
     *
     * @param obj 原始对象
     * @return 包装类
     */
    public static AssessmentResultVo objToVo(AssessmentResult obj) {
        if (obj == null) {
            return null;
        }
        AssessmentResultVo assessmentResultVo = new AssessmentResultVo();
        BeanUtil.copyProperties(obj, assessmentResultVo);
        return assessmentResultVo;
    }

    /**
     * 包装类转对象
     *
     * @param vo 包装类
     * @return 对象
     */
    public static AssessmentResult voToObj(AssessmentResultVo vo) {
        if (vo == null) {
            return null;
        }
        AssessmentResult assessmentResult = new AssessmentResult();
        BeanUtil.copyProperties(vo, assessmentResult);
        return assessmentResult;
    }
}
