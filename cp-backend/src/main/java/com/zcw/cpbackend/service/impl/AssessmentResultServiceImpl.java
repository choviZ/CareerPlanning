package com.zcw.cpbackend.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.mapper.AssessmentResultMapper;
import com.zcw.cpbackend.model.entity.AssessmentResult;
import com.zcw.cpbackend.service.AssessmentResultService;
import org.springframework.stereotype.Service;

/**
 * 测评结果表 服务层实现。
 *
 * @author zcw
 */
@Service
public class AssessmentResultServiceImpl extends ServiceImpl<AssessmentResultMapper, AssessmentResult>  implements AssessmentResultService{

}
