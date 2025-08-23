package com.zcw.cpbackend.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.zcw.cpbackend.model.entity.UserAssessment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户评估记录Vo。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAssessmentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 评估类型
     */
    private String testType;

    /**
     * 结果代码
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
     * 图标
     */
    private String resultIcon;

    /**
     * 用户选择的选项列表
     */
    private String choices;

    /**
     * 各维度的得分-Json
     */
    private String dimensionScores;

    /**
     * 用户ID
     */
    private Long userId;

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
    private String careerDescription;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 是否删除
     */
    private int isDeleted;


    public static UserAssessmentVo objToVo(UserAssessment userAssessment) {
        if (userAssessment == null) {
            return null;
        }
        UserAssessmentVo userAssessmentVo = new UserAssessmentVo();
        BeanUtil.copyProperties(userAssessment, userAssessmentVo);
        return userAssessmentVo;
    }


}
