package com.zcw.cpbackend.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.model.entity.UserAssessment;
import com.zcw.cpbackend.mapper.UserAssessmentMapper;
import com.zcw.cpbackend.service.UserAssessmentService;
import org.springframework.stereotype.Service;

/**
 * 用户评估记录表 服务层实现。
 *
 * @author zcw
 */
@Service
public class UserAssessmentServiceImpl extends ServiceImpl<UserAssessmentMapper, UserAssessment>  implements UserAssessmentService{

}
