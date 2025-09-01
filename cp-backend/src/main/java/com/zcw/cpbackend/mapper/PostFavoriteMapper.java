package com.zcw.cpbackend.mapper;

import com.mybatisflex.core.BaseMapper;
import com.zcw.cpbackend.model.entity.PostFavorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户收藏表 映射层。
 *
 * @author zcw
 */
@Mapper
public interface PostFavoriteMapper extends BaseMapper<PostFavorite> {

}