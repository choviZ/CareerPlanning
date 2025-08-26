package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.dto.resume.ResumeAddRequest;
import com.zcw.cpbackend.model.dto.resume.ResumeQueryRequest;
import com.zcw.cpbackend.model.dto.resume.ResumeUpdateRequest;
import com.zcw.cpbackend.model.entity.resume.Resume;
import com.zcw.cpbackend.model.vo.ResumeVo;

/**
 * 简历服务接口
 *
 * @author zcw
 */
public interface ResumeService extends IService<Resume> {

    /**
     * 添加简历
     *
     * @param resumeAddRequest 添加请求
     * @param loginUserId      当前登录用户ID
     * @return 简历ID
     */
    Long addResume(ResumeAddRequest resumeAddRequest, Long loginUserId);

    /**
     * 删除简历
     *
     * @param id          简历ID
     * @param loginUserId 当前登录用户ID
     * @return 是否删除成功
     */
    Boolean deleteResume(Long id, Long loginUserId);

    /**
     * 更新简历
     *
     * @param resumeUpdateRequest 更新请求
     * @param loginUserId         当前登录用户ID
     * @return 是否更新成功
     */
    Boolean updateResume(ResumeUpdateRequest resumeUpdateRequest, Long loginUserId);

    /**
     * 分页查询简历
     *
     * @param resumeQueryRequest 查询请求
     * @param loginUserId        当前登录用户ID
     * @return 分页结果
     */
    Page<ResumeVo> queryResume(ResumeQueryRequest resumeQueryRequest, Long loginUserId);

    /**
     * 根据ID查询简历
     *
     * @param id          简历ID
     * @param loginUserId 当前登录用户ID
     * @return 简历VO
     */
    ResumeVo queryResumeById(Long id, Long loginUserId);

    /**
     * 根据分享码查询简历
     *
     * @param shareCode 分享码
     * @return 简历VO
     */
    ResumeVo queryResumeByShareCode(String shareCode);

    /**
     * 复制简历
     *
     * @param id          原简历ID
     * @param loginUserId 当前登录用户ID
     * @return 新简历ID
     */
    Long copyResume(Long id, Long loginUserId);

    /**
     * 生成分享码
     *
     * @param id          简历ID
     * @param loginUserId 当前登录用户ID
     * @return 分享码
     */
    String generateShareCode(Long id, Long loginUserId);

    /**
     * 发布简历
     *
     * @param id          简历ID
     * @param loginUserId 当前登录用户ID
     * @return 是否发布成功
     */
    Boolean publishResume(Long id, Long loginUserId);
}