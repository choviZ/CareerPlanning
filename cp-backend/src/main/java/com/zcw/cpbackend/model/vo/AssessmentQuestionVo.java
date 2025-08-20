package com.zcw.cpbackend.model.vo;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zcw.cpbackend.model.dto.OptionDTO;
import com.zcw.cpbackend.model.entity.AssessmentQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 测评题目表 VO类。
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentQuestionVo implements Serializable {

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
     * 题目内容，如“你更喜欢独处还是社交？”
     */
    private String content;

    /**
     * 维度标识，如 MBTI 的 E/I, S/N, T/F, J/P；霍兰德的 R/I/A/S/E/C
     */
    private String dimension;

    /**
     * 选项数组，包含内容和分数规则
     */
    private List<OptionDTO> options;

    /**
     * 题目排序序号
     */
    private Integer sortOrder;

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 对象转包装类
     *
     * @param obj 原始对象
     * @return 包装类
     */
    public static AssessmentQuestionVo objToVo(AssessmentQuestion obj) {
        if (obj == null) {
            return null;
        }
        AssessmentQuestionVo assessmentQuestionVo = new AssessmentQuestionVo();
        BeanUtil.copyProperties(obj, assessmentQuestionVo, "options");
        // 处理不一致的格式
        if (StrUtil.isNotBlank(obj.getOptions())) {
            List<OptionDTO> optionDTOList = JSONUtil.toList(obj.getOptions(), OptionDTO.class);
            assessmentQuestionVo.setOptions(optionDTOList);
        }
        return assessmentQuestionVo;
    }

    /**
     * 包装类转对象
     *
     * @param vo 包装类
     * @return 对象
     */
    public static AssessmentQuestion voToObj(AssessmentQuestionVo vo) {
        if (vo == null) {
            return null;
        }
        AssessmentQuestion assessmentQuestion = new AssessmentQuestion();
        BeanUtil.copyProperties(vo, assessmentQuestion);
        assessmentQuestion.setOptions(JSONUtil.toJsonStr(vo.getOptions()));
        return assessmentQuestion;
    }
}
