package com.zcw.cpbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.exception.BusinessException;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.exception.ThrowUtils;
import com.zcw.cpbackend.model.dto.PostAddRequest;
import com.zcw.cpbackend.model.dto.PostEditRequest;
import com.zcw.cpbackend.model.dto.PostQueryRequest;
import com.zcw.cpbackend.model.entity.Post;
import com.zcw.cpbackend.model.entity.User;
import com.zcw.cpbackend.mapper.PostMapper;
import com.zcw.cpbackend.model.vo.PostVO;
import com.zcw.cpbackend.model.vo.UserVO;
import com.zcw.cpbackend.service.PostService;
import com.zcw.cpbackend.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子表 服务层实现。
 *
 * @author zcw
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Resource
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addPost(PostAddRequest postAddRequest, Long userId) {
        // 校验
        ThrowUtils.throwIf(postAddRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.NOT_LOGIN_ERROR);

        String title = postAddRequest.getTitle();
        String content = postAddRequest.getContent();
        String tags = postAddRequest.getTags();

        ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR, "帖子标题不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(content), ErrorCode.PARAMS_ERROR, "帖子内容不能为空");

        // 创建帖子对象
        Post post = Post.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .tags(tags)
                .viewCount(0)
                .likeCount(0)
                .commentCount(0)
                .isEssence(0)
                .status(0) // TODO 审核状态
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(0)
                .build();

        boolean result = this.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "发布帖子失败");
        return post.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editPost(PostEditRequest postEditRequest, Long userId) {
        ThrowUtils.throwIf(postEditRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.NOT_LOGIN_ERROR);

        Long postId = postEditRequest.getId();
        String title = postEditRequest.getTitle();
        String content = postEditRequest.getContent();
        String tags = postEditRequest.getTags();

        ThrowUtils.throwIf(postId == null || postId <= 0, ErrorCode.PARAMS_ERROR, "帖子ID不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR, "帖子标题不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(content), ErrorCode.PARAMS_ERROR, "帖子内容不能为空");

        // 查询原帖子
        Post oldPost = this.getById(postId);
        ThrowUtils.throwIf(oldPost == null, ErrorCode.NOT_FOUND_ERROR, "帖子不存在");
        ThrowUtils.throwIf(oldPost.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "帖子已删除");
        ThrowUtils.throwIf(!oldPost.getUserId().equals(userId), ErrorCode.NO_AUTH_ERROR, "无权限编辑此帖子");

        // 更新帖子
        Post updatePost = new Post();
        updatePost.setId(postId);
        updatePost.setTitle(title);
        updatePost.setContent(content);
        updatePost.setTags(tags);
        updatePost.setUpdatedAt(LocalDateTime.now());

        return this.updateById(updatePost);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePost(Long postId, Long userId) {
        ThrowUtils.throwIf(postId == null || postId <= 0, ErrorCode.PARAMS_ERROR, "帖子ID不能为空");
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.NOT_LOGIN_ERROR);

        // 查询帖子
        Post post = this.getById(postId);
        ThrowUtils.throwIf(post == null, ErrorCode.NOT_FOUND_ERROR, "帖子不存在");
        ThrowUtils.throwIf(post.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "帖子已删除");
        ThrowUtils.throwIf(!post.getUserId().equals(userId), ErrorCode.NO_AUTH_ERROR, "无权限删除此帖子");

        // 逻辑删除
        Post updatePost = new Post();
        updatePost.setId(postId);
        updatePost.setIsDeleted(1);
        updatePost.setUpdatedAt(LocalDateTime.now());

        return this.updateById(updatePost);
    }

    @Override
    public Page<PostVO> queryPostPage(PostQueryRequest postQueryRequest) {
        int current = postQueryRequest.getCurrent();
        int pageSize = postQueryRequest.getPageSize();
        String sortField = postQueryRequest.getSortField();
        String sortOrder = postQueryRequest.getSortOrder();

        // 构建查询条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(Post::getIsDeleted, 0)
                .eq(Post::getStatus, 0); // 只查询正常状态的帖子

        // 添加查询条件
        if (StrUtil.isNotBlank(postQueryRequest.getTitle())) {
            queryWrapper.like(Post::getTitle, postQueryRequest.getTitle());
        }
        if (StrUtil.isNotBlank(postQueryRequest.getContent())) {
            queryWrapper.like(Post::getContent, postQueryRequest.getContent());
        }
        if (StrUtil.isNotBlank(postQueryRequest.getTags())) {
            queryWrapper.like(Post::getTags, postQueryRequest.getTags());
        }
        if (postQueryRequest.getUserId() != null) {
            queryWrapper.eq(Post::getUserId, postQueryRequest.getUserId());
        }
        if (postQueryRequest.getIsEssence() != null) {
            queryWrapper.eq(Post::getIsEssence, postQueryRequest.getIsEssence());
        }

        // 排序
        if (StrUtil.isNotBlank(sortField)) {
            boolean isAsc = "asc".equals(sortOrder);
            switch (sortField) {
                case "created_at":
                    queryWrapper.orderBy(Post::getCreatedAt, isAsc);
                    break;
                case "view_count":
                    queryWrapper.orderBy(Post::getViewCount, isAsc);
                    break;
                case "like_count":
                    queryWrapper.orderBy(Post::getLikeCount, isAsc);
                    break;
                case "comment_count":
                    queryWrapper.orderBy(Post::getCommentCount, isAsc);
                    break;
                case "last_comment_at":
                    queryWrapper.orderBy(Post::getLastCommentAt, isAsc);
                    break;
                default:
                    queryWrapper.orderBy(Post::getCreatedAt, false);
            }
        } else {
            queryWrapper.orderBy(Post::getCreatedAt, false);
        }

        // 分页查询
        Page<Post> postPage = this.page(Page.of(current, pageSize), queryWrapper);

        // 转换为VO
        return convertToPostVOPage(postPage, null);
    }

    @Override
    public PostVO getPostById(Long postId, Long userId) {
        ThrowUtils.throwIf(postId == null || postId <= 0, ErrorCode.PARAMS_ERROR, "帖子ID不能为空");

        Post post = this.getById(postId);
        ThrowUtils.throwIf(post == null, ErrorCode.NOT_FOUND_ERROR, "帖子不存在");
        ThrowUtils.throwIf(post.getIsDeleted() == 1, ErrorCode.NOT_FOUND_ERROR, "帖子已删除");

        return convertToPostVO(post, userId);
    }

    @Override
    public Page<PostVO> getUserPosts(Long userId, Integer current, Integer pageSize) {
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");

        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(Post::getUserId, userId)
                .eq(Post::getIsDeleted, 0)
                .orderBy(Post::getCreatedAt, false);

        Page<Post> postPage = this.page(Page.of(current, pageSize), queryWrapper);
        return convertToPostVOPage(postPage, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long postId) {
        ThrowUtils.throwIf(postId == null || postId <= 0, ErrorCode.PARAMS_ERROR, "帖子ID不能为空");

        Post post = this.getById(postId);
        if (post != null && post.getIsDeleted() == 0) {
            Post updatePost = new Post();
            updatePost.setId(postId);
            updatePost.setViewCount(post.getViewCount() + 1);
            this.updateById(updatePost);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePostStats(Long postId, Integer likeCount, Integer commentCount) {
        ThrowUtils.throwIf(postId == null || postId <= 0, ErrorCode.PARAMS_ERROR, "帖子ID不能为空");

        Post post = this.getById(postId);
        if (post != null && post.getIsDeleted() == 0) {
            Post updatePost = new Post();
            updatePost.setId(postId);
            if (likeCount != null) {
                updatePost.setLikeCount(Math.max(0, post.getLikeCount() + likeCount));
            }
            if (commentCount != null) {
                updatePost.setCommentCount(Math.max(0, post.getCommentCount() + commentCount));
                if (commentCount > 0) {
                    updatePost.setLastCommentAt(LocalDateTime.now());
                }
            }
            this.updateById(updatePost);
        }
    }

    /**
     * 转换Post分页为PostVO分页
     */
    private Page<PostVO> convertToPostVOPage(Page<Post> postPage, Long currentUserId) {
        List<PostVO> postVOList = postPage.getRecords().stream()
                .map(post -> convertToPostVO(post, currentUserId))
                .collect(Collectors.toList());

        Page<PostVO> postVOPage = new Page<>();
        BeanUtils.copyProperties(postPage, postVOPage);
        postVOPage.setRecords(postVOList);
        return postVOPage;
    }

    /**
     * 转换Post为PostVO
     */
    private PostVO convertToPostVO(Post post, Long currentUserId) {
        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post, postVO);

        // 处理标签
        if (StrUtil.isNotBlank(post.getTags())) {
            try {
                List<String> tagList = JSONUtil.toList(post.getTags(), String.class);
                postVO.setTagList(tagList);
            } catch (Exception e) {
                // 如果JSON解析失败，按逗号分割
                postVO.setTagList(Arrays.asList(post.getTags().split(",")));
            }
        }

        // 获取用户信息
        User user = userService.getById(post.getUserId());
        if (user != null) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            postVO.setUser(userVO);
        }

        // TODO: 设置是否已点赞、是否已收藏（需要实现相关功能后补充）
        postVO.setHasLiked(false);
        postVO.setHasFavorited(false);

        return postVO;
    }
}
