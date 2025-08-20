package com.zcw.cpbackend.controller;

import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.model.dto.AddQuestionRequest;
import com.zcw.cpbackend.model.dto.UpdateQuestionRequest;
import com.zcw.cpbackend.model.vo.AssessmentQuestionVo;
import com.zcw.cpbackend.service.AssessmentQuestionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测评题目表 控制层。
 *
 * @author zcw
 */
@Slf4j
@RestController
@RequestMapping("/assessment/question")
public class AssessmentQuestionController {

    @Resource
    private AssessmentQuestionService assessmentQuestionService;

    // region 增删改查
    /**
     * 添加题目
     */
    @PostMapping("/addQuestion")
    public BaseResponse<Boolean> addQuestion(@RequestBody AddQuestionRequest request) {
        return ResultUtils.success(assessmentQuestionService.addQuestion(request));
    }

    /**
     * 删除题目
     */
    @GetMapping("/del/{id}")
    public BaseResponse<Boolean> deleteQuestion(@PathVariable Long id) {
        return ResultUtils.success(assessmentQuestionService.deleteQuestion(id));
    }

    /**
     * 修改题目
     */
    @PostMapping("/updateQuestion")
    public BaseResponse<Boolean> updateQuestion(@RequestBody UpdateQuestionRequest updateQuestionRequest) {
        boolean result = assessmentQuestionService.updateQuestion(updateQuestionRequest);
        return ResultUtils.success(result);
    }

    /**
     * 查询题目
     */
    @GetMapping("/list")
    public BaseResponse<List<AssessmentQuestionVo>> queryQuestion(@RequestParam String testType) {
        List<AssessmentQuestionVo> list = assessmentQuestionService.queryQuestion(testType);
        return ResultUtils.success(list);
    }
    // endregion
}
