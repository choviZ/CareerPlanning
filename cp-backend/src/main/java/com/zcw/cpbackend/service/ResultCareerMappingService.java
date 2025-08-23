package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.dto.assessment.UpdateResultCareerRequest;
import com.zcw.cpbackend.model.entity.ResultCareerMapping;

/**
 * 测评结果职业关联表 服务层。
 *
 * @author zcw
 */
public interface ResultCareerMappingService extends IService<ResultCareerMapping> {

    /**
     * 添加测评结果职业关联
     *
     * @param assessmentResultId 测评结果id
     * @param careerId           职业id
     * @param compatibilityScore 兼容性评分
     * @return 是否添加成功
     */
    boolean addResultCareerMapping(Long assessmentResultId, Long careerId, int compatibilityScore);

    /**
     * 修改测评结果职业关联
     *
     * @param updateResultCareerRequest 修改测评结果职业关联请求
     * @return 是否修改成功
     */
    boolean updateResultCareerMapping(UpdateResultCareerRequest updateResultCareerRequest);

    /**
     * 删除测评结果职业关联
     *
     * @param id id
     * @return 是否删除成功
     */
    boolean deleteResultCareerMapping(Long id);

    /**
     * 根据测评结果查询结果职业关联表
     * @param testType 评估类型
     * @param resultCode 评估结果
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 结果职业关联表
     */
    Page<ResultCareerMapping> queryMappingByResultCode(String testType, String resultCode, int pageNum, int pageSize);

    /**
     * 查询最匹配的结果职业映射
     * @param testType 评估类型
     * @param resultCode 评估结果
     * @return 最匹配的结果职业映射
     */
    ResultCareerMapping queryBastCompatibleCareer(String testType, String resultCode);
}
