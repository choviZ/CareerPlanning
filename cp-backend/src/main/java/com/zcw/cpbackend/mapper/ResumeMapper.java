package com.zcw.cpbackend.mapper;

import com.mybatisflex.core.BaseMapper;
import com.zcw.cpbackend.model.entity.Resume;
import org.apache.ibatis.annotations.Mapper;

/**
 * 简历表 映射层。
 *
 * @author zcw
 */
@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {

}