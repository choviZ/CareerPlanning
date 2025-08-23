package com.zcw.cpbackend.model.dto.assessment;

import lombok.Data;

/**
 * 修改评估结果和职业关系请求
 */
@Data
public class UpdateResultCareerRequest {

    private long id;

    private long assessmentResultId;

    private long careerId;

    private int compatibilityScore;
}
