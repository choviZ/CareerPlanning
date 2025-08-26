package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.dto.resumetemplate.ResumeTemplateAddRequest;
import com.zcw.cpbackend.model.dto.resumetemplate.ResumeTemplateQueryRequest;
import com.zcw.cpbackend.model.dto.resumetemplate.ResumeTemplateUpdateRequest;
import com.zcw.cpbackend.model.entity.resume.template.ResumeTemplate;
import com.zcw.cpbackend.model.vo.ResumeTemplateVo;

import java.util.List;

/**
 * 简历模板服务接口
 *
 * @author zcw
 */
public interface ResumeTemplateService extends IService<ResumeTemplate> {

    /**
     * 添加简历模板
     *
     * @param resumeTemplateAddRequest 添加请求
     * @return 模板ID
     */
    Long addResumeTemplate(ResumeTemplateAddRequest resumeTemplateAddRequest);

    /**
     * 删除简历模板
     *
     * @param id 模板ID
     * @return 是否删除成功
     */
    Boolean deleteResumeTemplate(Long id);

    /**
     * 更新简历模板
     *
     * @param resumeTemplateUpdateRequest 更新请求
     * @return 是否更新成功
     */
    Boolean updateResumeTemplate(ResumeTemplateUpdateRequest resumeTemplateUpdateRequest);

    /**
     * 分页查询简历模板
     *
     * @param resumeTemplateQueryRequest 查询请求
     * @return 分页结果
     */
    Page<ResumeTemplateVo> queryResumeTemplate(ResumeTemplateQueryRequest resumeTemplateQueryRequest);

    /**
     * 根据ID查询简历模板
     *
     * @param id 模板ID
     * @return 模板VO
     */
    ResumeTemplateVo queryResumeTemplateById(Long id);

    /**
     * 获取可用的简历模板列表（用户端）
     *
     * @return 模板列表
     */
    List<ResumeTemplateVo> getAvailableTemplates();

    /**
     * 增加模板使用次数
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    Boolean incrementUseCount(Long templateId);

    /**
     * 启用/禁用模板
     *
     * @param id     模板ID
     * @param status 状态：0-禁用，1-启用
     * @return 是否成功
     */
    Boolean updateTemplateStatus(Long id, Integer status);
}