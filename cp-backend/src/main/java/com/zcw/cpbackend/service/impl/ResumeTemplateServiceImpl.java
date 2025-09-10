package com.zcw.cpbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.BusinessException;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.mapper.ResumeTemplateMapper;
import com.zcw.cpbackend.model.dto.resumetemplate.ResumeTemplateAddRequest;
import com.zcw.cpbackend.model.dto.resumetemplate.ResumeTemplateQueryRequest;
import com.zcw.cpbackend.model.dto.resumetemplate.ResumeTemplateUpdateRequest;
import com.zcw.cpbackend.model.entity.ResumeTemplate;
import com.zcw.cpbackend.model.vo.ResumeTemplateVo;
import com.zcw.cpbackend.service.ResumeTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 简历模板服务实现类
 *
 * @author zcw
 */
@Service
public class ResumeTemplateServiceImpl extends ServiceImpl<ResumeTemplateMapper, ResumeTemplate> implements ResumeTemplateService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addResumeTemplate(ResumeTemplateAddRequest resumeTemplateAddRequest) {
        if (resumeTemplateAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String templateName = resumeTemplateAddRequest.getTemplateName();
        Integer templateType = resumeTemplateAddRequest.getTemplateType();
        Integer status = resumeTemplateAddRequest.getIsActive();

        if (StringUtils.isBlank(templateName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板名称不能为空");
        }
        if (templateType == null || (templateType != 1 && templateType != 2 && templateType != 3)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板类型参数错误");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态参数错误");
        }
        // 创建模板对象
        ResumeTemplate resumeTemplate = ResumeTemplate.builder()
                .templateName(templateName)
                .templateDesc(resumeTemplateAddRequest.getTemplateDesc())
                .previewUrl(resumeTemplateAddRequest.getPreviewUrl())
                .templateConfig(resumeTemplateAddRequest.getTemplateConfig())
                .defaultContent(resumeTemplateAddRequest.getDefaultContent())
                .templateType(templateType)
                .sortOrder(resumeTemplateAddRequest.getSortOrder() != null ? resumeTemplateAddRequest.getSortOrder() : 0)
                .isActive(status)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 保存模板
        boolean result = this.save(resumeTemplate);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加模板失败");
        }

        return resumeTemplate.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteResumeTemplate(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 查询模板是否存在
        ResumeTemplate resumeTemplate = this.getById(id);
        if (resumeTemplate == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }

        // 删除模板
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateResumeTemplate(ResumeTemplateUpdateRequest resumeTemplateUpdateRequest) {
        if (resumeTemplateUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = resumeTemplateUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板ID不能为空");
        }

        // 查询模板是否存在
        ResumeTemplate existingTemplate = this.getById(id);
        if (existingTemplate == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }

        // 构建更新对象
        ResumeTemplate resumeTemplate = ResumeTemplate.builder()
                .id(id)
                .templateName(resumeTemplateUpdateRequest.getTemplateName())
                .templateDesc(resumeTemplateUpdateRequest.getTemplateDesc())
                .previewUrl(resumeTemplateUpdateRequest.getPreviewUrl())
                .templateConfig(resumeTemplateUpdateRequest.getTemplateConfig())
                .defaultContent(resumeTemplateUpdateRequest.getDefaultContent())
                .templateType(resumeTemplateUpdateRequest.getTemplateType())
                .sortOrder(resumeTemplateUpdateRequest.getSortOrder())
                .isActive(resumeTemplateUpdateRequest.getStatus())
                .updatedAt(LocalDateTime.now())
                .build();

        // 更新模板
        return this.updateById(resumeTemplate);
    }

    @Override
    public Page<ResumeTemplateVo> queryResumeTemplate(ResumeTemplateQueryRequest resumeTemplateQueryRequest) {
        if (resumeTemplateQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 构建查询条件
        QueryWrapper queryWrapper = buildQueryWrapper(resumeTemplateQueryRequest);

        // 分页查询
        Page<ResumeTemplate> templatePage = this.page(Page.of(resumeTemplateQueryRequest.getCurrent(), resumeTemplateQueryRequest.getPageSize()), queryWrapper);

        // 转换为VO
        List<ResumeTemplateVo> templateVoList = templatePage.getRecords().stream()
                .map(ResumeTemplateVo::objToVo)
                .collect(Collectors.toList());

        // 构建分页结果
        Page<ResumeTemplateVo> templateVoPage = new Page<>();
        templateVoPage.setRecords(templateVoList);
        templateVoPage.setTotalRow(templatePage.getTotalRow());
        templateVoPage.setPageNumber(templatePage.getPageNumber());
        templateVoPage.setPageSize(templatePage.getPageSize());

        return templateVoPage;
    }

    @Override
    public ResumeTemplateVo queryResumeTemplateById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 查询模板
        ResumeTemplate resumeTemplate = this.getById(id);
        if (resumeTemplate == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }

        return ResumeTemplateVo.objToVo(resumeTemplate);
    }

    @Override
    public List<ResumeTemplateVo> getAvailableTemplates() {
        // 查询启用状态的模板，按排序权重和创建时间排序
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(ResumeTemplate::getIsActive,1)
                .orderBy(ResumeTemplate::getSortOrder, false)
                .orderBy(ResumeTemplate::getCreatedAt, false);

        List<ResumeTemplate> templates = this.list(queryWrapper);
        return templates.stream()
                .map(ResumeTemplateVo::objToVo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean incrementUseCount(Long templateId) {
        if (templateId == null || templateId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 查询模板
        ResumeTemplate resumeTemplate = this.getById(templateId);
        if (resumeTemplate == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }
        return this.updateById(resumeTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTemplateStatus(Long id, Integer status) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态参数错误");
        }

        // 查询模板是否存在
        ResumeTemplate resumeTemplate = this.getById(id);
        if (resumeTemplate == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }

        // 更新状态
        ResumeTemplate updateTemplate = ResumeTemplate.builder()
                .id(id)
                .isActive(status)
                .updatedAt(LocalDateTime.now())
                .build();

        return this.updateById(updateTemplate);
    }

    /**
     * 构建查询条件
     *
     * @param resumeTemplateQueryRequest 查询请求
     * @return 查询条件
     */
    private QueryWrapper buildQueryWrapper(ResumeTemplateQueryRequest resumeTemplateQueryRequest) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        String templateName = resumeTemplateQueryRequest.getTemplateName();
        Integer templateType = resumeTemplateQueryRequest.getTemplateType();
        Integer status = resumeTemplateQueryRequest.getIsActive();
        // 构造查询条件
        if (StrUtil.isNotBlank(templateName)) {
            queryWrapper.like(ResumeTemplate::getTemplateName, templateName);
        }
        if (templateType != null) {
            queryWrapper.eq(ResumeTemplate::getTemplateType, templateType);
        }
        if (status != null) {
            queryWrapper.eq(ResumeTemplate::getIsActive, status);
        }
        // 按排序权重倒序，创建时间倒序
        queryWrapper.orderBy(ResumeTemplate::getSortOrder, false);
        queryWrapper.orderBy(ResumeTemplate::getCreatedAt, false);
        return queryWrapper;
    }

}