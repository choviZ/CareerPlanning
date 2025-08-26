package com.zcw.cpbackend.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.BusinessException;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.mapper.ResumeMapper;
import com.zcw.cpbackend.model.dto.resume.ResumeAddRequest;
import com.zcw.cpbackend.model.dto.resume.ResumeQueryRequest;
import com.zcw.cpbackend.model.dto.resume.ResumeUpdateRequest;
import com.zcw.cpbackend.model.entity.resume.Resume;
import com.zcw.cpbackend.model.dto.resume.BasicInfo;
import com.zcw.cpbackend.model.dto.resume.ResumeContent;
import com.zcw.cpbackend.model.vo.ResumeVo;
import com.zcw.cpbackend.service.ResumeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 简历服务实现类
 *
 * @author zcw
 */
@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements ResumeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addResume(ResumeAddRequest resumeAddRequest, Long loginUserId) {
        if (resumeAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (loginUserId == null || loginUserId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        String title = resumeAddRequest.getTitle();
        ResumeContent content = resumeAddRequest.getContent();
        Integer status = resumeAddRequest.getStatus();
        Integer isPublic = resumeAddRequest.getIsPublic();

        if (StringUtils.isBlank(title)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "简历标题不能为空");
        }
        if (status == null || (status != 1 && status != 2 && status != 3)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态参数错误");
        }
        if (isPublic == null || (isPublic != 0 && isPublic != 1)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公开状态参数错误");
        }

        // 创建简历对象
        Resume resume = Resume.builder()
                .userId(loginUserId)
                .title(title)
                .templateId(resumeAddRequest.getTemplateId())
                .content(content)
                .status(status)
                .isPublic(isPublic)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 提取冗余字段
        extractRedundantFields(resume);

        // 保存简历
        boolean result = this.save(resume);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加简历失败");
        }

        return resume.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteResume(Long id, Long loginUserId) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查询简历是否存在
        Resume resume = this.getById(id);
        if (resume == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "简历不存在");
        }
        // 检查权限（管理员可以删除任何简历）
        if (loginUserId != null && !Objects.equals(resume.getUserId(), loginUserId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除此简历");
        }
        // 删除简历
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateResume(ResumeUpdateRequest resumeUpdateRequest, Long loginUserId) {
        if (resumeUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (loginUserId == null || loginUserId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        Long id = resumeUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "简历ID不能为空");
        }

        // 查询简历是否存在
        Resume existingResume = this.getById(id);
        if (existingResume == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "简历不存在");
        }

        // 检查权限
        if (!Objects.equals(existingResume.getUserId(), loginUserId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限修改此简历");
        }

        // 构建更新对象
        Resume resume = Resume.builder()
                .id(id)
                .title(resumeUpdateRequest.getTitle())
                .templateId(resumeUpdateRequest.getTemplateId())
                .content(resumeUpdateRequest.getContent())
                .status(resumeUpdateRequest.getStatus())
                .isPublic(resumeUpdateRequest.getIsPublic())
                .updatedAt(LocalDateTime.now())
                .build();

        // 提取冗余字段
        extractRedundantFields(resume);

        // 更新简历
        return this.updateById(resume);
    }

    @Override
    public Page<ResumeVo> queryResume(ResumeQueryRequest resumeQueryRequest, Long loginUserId) {
        if (resumeQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 构建查询条件
        QueryWrapper queryWrapper = buildQueryWrapper(resumeQueryRequest, loginUserId);

        // 分页查询
        Page<Resume> resumePage = this.page(Page.of(resumeQueryRequest.getCurrent(), resumeQueryRequest.getPageSize()), queryWrapper);

        // 转换为VO
        List<ResumeVo> resumeVoList = resumePage.getRecords().stream()
                .map(ResumeVo::objToVo)
                .collect(Collectors.toList());

        // 构建分页结果
        Page<ResumeVo> resumeVoPage = new Page<>();
        resumeVoPage.setRecords(resumeVoList);
        resumeVoPage.setTotalRow(resumePage.getTotalRow());
        resumeVoPage.setPageNumber(resumePage.getPageNumber());
        resumeVoPage.setPageSize(resumePage.getPageSize());

        return resumeVoPage;
    }

    @Override
    public ResumeVo queryResumeById(Long id, Long loginUserId) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 查询简历
        Resume resume = this.getById(id);
        if (resume == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "简历不存在");
        }

        // 检查权限（管理员可以查看任何简历，普通用户只能查看自己的简历或公开的简历）
        if (loginUserId != null && !Objects.equals(resume.getUserId(), loginUserId) && !Objects.equals(resume.getIsPublic(), 1)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限查看此简历");
        }

        return ResumeVo.objToVo(resume);
    }

    @Override
    public ResumeVo queryResumeByShareCode(String shareCode) {
        if (StringUtils.isBlank(shareCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分享码不能为空");
        }
        // 构造查询条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(Resume::getShareCode,shareCode)
                .eq(Resume::getIsPublic,1);// 只能分享公开的简历
        // 根据分享码查询简历
        Resume resume = this.getOne(queryWrapper);
        if (resume == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "简历不存在或未公开");
        }

        return ResumeVo.objToVo(resume);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyResume(Long id, Long loginUserId) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (loginUserId == null || loginUserId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 查询原简历
        Resume originalResume = this.getById(id);
        if (originalResume == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "简历不存在");
        }

        // 检查权限（只能复制自己的简历或公开的简历）
        if (!Objects.equals(originalResume.getUserId(), loginUserId) && !Objects.equals(originalResume.getIsPublic(), 1)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限复制此简历");
        }

        // 创建新简历
        Resume newResume = Resume.builder()
                .userId(loginUserId)
                .title(originalResume.getTitle() + "（副本）")
                .templateId(originalResume.getTemplateId())
                .content(originalResume.getContent())
                .name(originalResume.getName())
                .jobIntention(originalResume.getJobIntention())
                .status(1) // 复制的简历默认为草稿状态
                .isPublic(0) // 复制的简历默认为私有
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 保存新简历
        boolean result = this.save(newResume);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "复制简历失败");
        }

        return newResume.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String generateShareCode(Long id, Long loginUserId) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (loginUserId == null || loginUserId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 查询简历
        Resume resume = this.getById(id);
        if (resume == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "简历不存在");
        }

        // 检查权限
        if (!Objects.equals(resume.getUserId(), loginUserId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限操作此简历");
        }

        // 检查简历是否已发布
        if (!Objects.equals(resume.getStatus(), 3)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只有已发布的简历才能生成分享码");
        }

        // 生成分享码
        String shareCode = IdUtil.randomUUID().replace("-", "").substring(0, 16);

        // 更新简历
        Resume updateResume = Resume.builder()
                .id(id)
                .shareCode(shareCode)
                .isPublic(1) // 生成分享码时自动设为公开
                .updatedAt(LocalDateTime.now())
                .build();

        boolean result = this.updateById(updateResume);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "生成分享码失败");
        }

        return shareCode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean publishResume(Long id, Long loginUserId) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (loginUserId == null || loginUserId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 查询简历
        Resume resume = this.getById(id);
        if (resume == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "简历不存在");
        }

        // 检查权限
        if (!Objects.equals(resume.getUserId(), loginUserId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限操作此简历");
        }

        // 检查简历内容是否完整
        if (resume.getContent() == null || resume.getContent().getBasicInfo() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "简历内容不完整，无法发布");
        }

        // 更新简历状态
        Resume updateResume = Resume.builder()
                .id(id)
                .status(3) // 设为已发布
                .updatedAt(LocalDateTime.now())
                .build();

        return this.updateById(updateResume);
    }

    /**
     * 构建查询条件
     *
     * @param resumeQueryRequest 查询请求
     * @param loginUserId        当前登录用户ID（null表示管理员查询）
     * @return 查询条件
     */
    private QueryWrapper buildQueryWrapper(ResumeQueryRequest resumeQueryRequest, Long loginUserId) {
        QueryWrapper queryWrapper = QueryWrapper.create();

        String title = resumeQueryRequest.getTitle();
        String name = resumeQueryRequest.getName();
        String jobIntention = resumeQueryRequest.getJobIntention();
        Integer status = resumeQueryRequest.getStatus();
        Integer isPublic = resumeQueryRequest.getIsPublic();
        Long templateId = resumeQueryRequest.getTemplateId();
        Long userId = resumeQueryRequest.getUserId();

        // 如果是普通用户查询，只查询当前用户的简历
        if (loginUserId != null) {
            queryWrapper.eq(Resume::getUserId, loginUserId);
        } else {
            // 管理员查询，可以查询指定用户的简历
            if (userId != null && userId > 0) {
                queryWrapper.eq(Resume::getUserId,userId);
            }
        }

        if (StrUtil.isNotBlank(title)) {
            queryWrapper.like(Resume::getTitle,title);
        }
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like(Resume::getName,name);
        }
        if (StrUtil.isNotBlank(jobIntention)) {
            queryWrapper.like(Resume::getJobIntention, jobIntention);
        }
        if (status != null) {
            queryWrapper.eq(Resume::getStatus, status);
        }
        if (isPublic != null) {
            queryWrapper.eq(Resume::getIsPublic, isPublic);
        }
        if (templateId != null && templateId > 0) {
            queryWrapper.eq(Resume::getTemplateId, templateId);
        }
        // 按更新时间倒序
        queryWrapper.orderBy(Resume::getUpdatedAt,false);

        return queryWrapper;
    }

    /**
     * 提取冗余字段
     *
     * @param resume 简历对象
     */
    private void extractRedundantFields(Resume resume) {
        if (resume.getContent() != null) {
            BasicInfo basicInfo = resume.getContent().getBasicInfo();
            if (basicInfo != null) {
                if (StrUtil.isNotBlank(basicInfo.getName())) {
                    resume.setName(basicInfo.getName());
                }
                if (StrUtil.isNotBlank(basicInfo.getJobIntention())) {
                    resume.setJobIntention(basicInfo.getJobIntention());
                }
            }
        }
    }
}