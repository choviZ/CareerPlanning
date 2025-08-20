package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.dto.AddQuestionRequest;
import com.zcw.cpbackend.model.dto.UpdateQuestionRequest;
import com.zcw.cpbackend.model.entity.AssessmentQuestion;
import com.zcw.cpbackend.model.vo.AssessmentQuestionVo;

import java.util.List;

/**
 * 测评题目表 服务层。
 *
 * @author zcw
 */
public interface AssessmentQuestionService extends IService<AssessmentQuestion> {

    /**
     * 添加题目
     * @param request 请求体
     * @return 添加结果
     */
    boolean addQuestion(AddQuestionRequest request);
    
    /**
     * 修改题目
     * @param request 请求体
     * @return 修改结果
     */
    boolean updateQuestion(UpdateQuestionRequest request);
    
    /**
     * 删除题目
     * @param id 题目ID
     * @return 删除结果
     */
    boolean deleteQuestion(Long id);

    /**
     * 查询题目列表
     * @param testType
     * @return
     */
    List<AssessmentQuestionVo> queryQuestion(String testType);
}
