package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.dto.learningresource.LearningResourceAddRequest;
import com.zcw.cpbackend.model.dto.learningresource.LearningResourceEditRequest;
import com.zcw.cpbackend.model.dto.learningresource.LearningResourceQueryRequest;
import com.zcw.cpbackend.model.entity.LearningResource;
import com.zcw.cpbackend.model.vo.LearningResourceVO;

/**
 * 学习资源表 服务层。
 *
 * @author zcw
 */
public interface LearningResourceService extends IService<LearningResource> {

    /**
     * 添加学习资源
     *
     * @param learningResourceAddRequest 学习资源添加请求
     * @return 学习资源ID
     */
    Long addLearningResource(LearningResourceAddRequest learningResourceAddRequest);

    /**
     * 编辑学习资源
     *
     * @param learningResourceEditRequest 学习资源编辑请求
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean editLearningResource(LearningResourceEditRequest learningResourceEditRequest, Long userId);

    /**
     * 删除学习资源（逻辑删除）
     *
     * @param resourceId 学习资源ID
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean deleteLearningResource(Long resourceId, Long userId);

    /**
     * 分页查询学习资源
     *
     * @param learningResourceQueryRequest 查询请求
     * @return 学习资源分页数据
     */
    Page<LearningResourceVO> queryLearningResourcePage(LearningResourceQueryRequest learningResourceQueryRequest);

    /**
     * 根据ID获取学习资源详情
     *
     * @param resourceId 学习资源ID
     * @return 学习资源详情
     */
    LearningResourceVO getLearningResourceById(Long resourceId);

    /**
     * 增加学习资源浏览量
     *
     * @param resourceId 学习资源ID
     */
    void incrementViewCount(Long resourceId);

    /**
     * 切换学习资源置顶状态
     *
     * @param resourceId 学习资源ID
     * @return 切换后的置顶状态（true-置顶，false-非置顶）
     */
    Boolean toggleTop(Long resourceId);

    /**
     * 根据分类名称查询学习资源
     *
     * @param category 分类名称
     * @param current 当前页
     * @param pageSize 页面大小
     * @return 学习资源分页数据
     */
    Page<LearningResourceVO> getLearningResourcesByCategory(String category, Integer current, Integer pageSize);


    /**
     * 搜索学习资源
     *
     * @param keyword 关键词
     * @param current 当前页
     * @param pageSize 页面大小
     * @return 学习资源分页数据
     */
    Page<LearningResourceVO> searchLearningResources(String keyword, Integer current, Integer pageSize);

}
