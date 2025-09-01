package com.zcw.cpbackend.mapper;

import com.mybatisflex.core.BaseMapper;
import com.zcw.cpbackend.model.entity.PostLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户点赞表 映射层。
 *
 * @author zcw
 */
@Mapper
public interface PostLikeMapper extends BaseMapper<PostLike> {

}