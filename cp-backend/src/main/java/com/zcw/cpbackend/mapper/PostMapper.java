package com.zcw.cpbackend.mapper;

import com.mybatisflex.core.BaseMapper;
import com.zcw.cpbackend.model.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * 帖子表 映射层。
 *
 * @author zcw
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

}
