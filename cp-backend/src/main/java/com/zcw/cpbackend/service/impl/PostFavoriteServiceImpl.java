package com.zcw.cpbackend.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.exception.ThrowUtils;
import com.zcw.cpbackend.mapper.PostFavoriteMapper;
import com.zcw.cpbackend.model.entity.Post;
import com.zcw.cpbackend.model.entity.PostFavorite;
import com.zcw.cpbackend.model.vo.PostVO;
import com.zcw.cpbackend.service.PostFavoriteService;
import com.zcw.cpbackend.service.PostService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户收藏表 服务层实现。
 *
 * @author zcw
 */
@Service
public class PostFavoriteServiceImpl extends ServiceImpl<PostFavoriteMapper, PostFavorite> implements PostFavoriteService {

    @Resource
    private PostService postService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean toggleFavorite(Long userId, Long postId) {
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(postId == null || postId <= 0, ErrorCode.PARAMS_ERROR, "帖子ID不能为空");

        // 检查帖子是否存在
        Post post = postService.getById(postId);
        ThrowUtils.throwIf(post == null, ErrorCode.NOT_FOUND_ERROR, "帖子不存在");
        ThrowUtils.throwIf(post.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "帖子已删除");

        // 检查是否已收藏
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(PostFavorite::getUserId, userId)
                .eq(PostFavorite::getPostId, postId);

        PostFavorite existingFavorite = this.getOne(queryWrapper);

        if (existingFavorite != null) {
            // 已收藏，取消收藏
            this.removeById(existingFavorite.getId());
            return false;
        } else {
            // 未收藏，添加收藏
            PostFavorite postFavorite = PostFavorite.builder()
                    .userId(userId)
                    .postId(postId)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            this.save(postFavorite);
            return true;
        }
    }

    @Override
    public Boolean hasFavorited(Long userId, Long postId) {
        if (userId == null || userId <= 0) {
            return false;
        }
        
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(PostFavorite::getUserId, userId)
                .eq(PostFavorite::getPostId, postId);

        return this.exists(queryWrapper);
    }

    @Override
    public Page<PostVO> getUserFavoritePosts(Long userId, Integer current, Integer pageSize) {
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");

        // 查询用户收藏的帖子ID列表
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(PostFavorite::getUserId, userId)
                .orderBy(PostFavorite::getCreatedAt, false);

        Page<PostFavorite> favoritePage = this.page(Page.of(current, pageSize), queryWrapper);
        
        if (favoritePage.getRecords().isEmpty()) {
            Page<PostVO> emptyPage = new Page<>();
            emptyPage.setPageNumber(current);
            emptyPage.setPageSize(pageSize);
            emptyPage.setTotalRow(0);
            return emptyPage;
        }

        // 获取帖子ID列表
        List<Long> postIds = favoritePage.getRecords().stream()
                .map(PostFavorite::getPostId)
                .collect(Collectors.toList());

        // 查询帖子详情
        QueryWrapper postQueryWrapper = QueryWrapper.create()
                .in(Post::getId, postIds)
                .eq(Post::getIsDeleted, 0)
                .eq(Post::getStatus, 0);

        List<Post> posts = postService.list(postQueryWrapper);
        
        // 转换为PostVO
        List<PostVO> postVOList = posts.stream()
                .map(post -> postService.getPostById(post.getId(), userId))
                .collect(Collectors.toList());

        // 构建分页结果
        Page<PostVO> postVOPage = new Page<>();
        postVOPage.setPageNumber(favoritePage.getPageNumber());
        postVOPage.setPageSize(favoritePage.getPageSize());
        postVOPage.setTotalRow(favoritePage.getTotalRow());
        postVOPage.setTotalPage(favoritePage.getTotalPage());
        postVOPage.setRecords(postVOList);

        return postVOPage;
    }
}