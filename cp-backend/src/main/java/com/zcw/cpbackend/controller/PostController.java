package com.zcw.cpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.model.dto.post.PostAddRequest;
import com.zcw.cpbackend.model.dto.post.PostEditRequest;
import com.zcw.cpbackend.model.dto.post.PostQueryRequest;
import com.zcw.cpbackend.model.vo.PostVO;
import com.zcw.cpbackend.model.vo.UserVO;
import com.zcw.cpbackend.service.PostFavoriteService;
import com.zcw.cpbackend.service.PostLikeService;
import com.zcw.cpbackend.service.PostService;
import com.zcw.cpbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 帖子表 控制层。
 *
 * @author zcw
 */
@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Resource
    private PostLikeService postLikeService;

    @Resource
    private PostFavoriteService postFavoriteService;

    /**
     * 发布帖子
     *
     * @param postAddRequest 帖子发布请求
     * @return 帖子ID
     */
    @PostMapping("/add")
    @SaCheckLogin
    public BaseResponse<Long> addPost(@RequestBody PostAddRequest postAddRequest) {
        Long userId = StpUtil.getLoginIdAsLong();
        Long postId = postService.addPost(postAddRequest, userId);
        return ResultUtils.success(postId);
    }

    /**
     * 编辑帖子
     *
     * @param postEditRequest 帖子编辑请求
     * @return 是否成功
     */
    @PostMapping("/edit")
    @SaCheckLogin
    public BaseResponse<Boolean> editPost(@RequestBody PostEditRequest postEditRequest) {
        Long userId = StpUtil.getLoginIdAsLong();
        Boolean result = postService.editPost(postEditRequest, userId);
        return ResultUtils.success(result);
    }

    /**
     * 删除帖子
     *
     * @param postId 帖子ID
     * @return 是否成功
     */
    @PostMapping("/delete/{postId}")
    @SaCheckLogin
    public BaseResponse<Boolean> deletePost(@PathVariable Long postId) {
        Long userId = StpUtil.getLoginIdAsLong();
        Boolean result = postService.deletePost(postId, userId);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询帖子
     *
     * @param postQueryRequest 查询请求
     * @return 帖子分页数据
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<PostVO>> queryPostPage(@RequestBody PostQueryRequest postQueryRequest, HttpServletRequest request) {
        // 获取当前用户ID（可能为null，表示未登录用户）
        Long userId = StpUtil.getLoginIdAsLong();
        Page<PostVO> postPage = postService.queryPostPage(postQueryRequest, userId);
        return ResultUtils.success(postPage);
    }

    /**
     * 根据ID获取帖子详情
     *
     * @param postId 帖子ID
     * @return 帖子详情
     */
    @GetMapping("/get/{postId}")
    public BaseResponse<PostVO> getPostById(@PathVariable Long postId) {
        Long userId = null;
        try {
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            // 用户未登录，userId为null
        }
        PostVO postVO = postService.getPostById(postId, userId);
        // 增加浏览量
        postService.incrementViewCount(postId);
        return ResultUtils.success(postVO);
    }

    /**
     * 查询用户发布的帖子
     *
     * @param current 当前页
     * @param pageSize 页面大小
     * @return 用户帖子分页数据
     */
    @GetMapping("/my")
    @SaCheckLogin
    public BaseResponse<Page<PostVO>> getUserPosts(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<PostVO> postPage = postService.getUserPosts(userId, current, pageSize);
        return ResultUtils.success(postPage);
    }

    /**
     * 查询指定用户发布的帖子
     *
     * @param userId 用户ID
     * @param current 当前页
     * @param pageSize 页面大小
     * @return 用户帖子分页数据
     */
    @GetMapping("/user/{userId}")
    public BaseResponse<Page<PostVO>> getUserPostsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<PostVO> postPage = postService.getUserPosts(userId, current, pageSize);
        return ResultUtils.success(postPage);
    }

    /**
     * 点赞/取消点赞帖子
     */
    @PostMapping("/like/{postId}")
    @Operation(summary = "点赞/取消点赞帖子")
    public BaseResponse<Boolean> toggleLike(@PathVariable Long postId, HttpServletRequest request) {
        UserVO currentUser = userService.getLoginUser(request);
        Boolean result = postLikeService.toggleLike(currentUser.getId(), 1, postId);
        return ResultUtils.success(result);
    }

    /**
     * 获取帖子点赞数量
     */
    @GetMapping("/like/count/{postId}")
    @Operation(summary = "获取帖子点赞数量")
    public BaseResponse<Long> getLikeCount(@PathVariable Long postId) {
        Long count = postLikeService.getLikeCount(1, postId);
        return ResultUtils.success(count);
    }

    /**
     * 检查是否已点赞
     */
    @GetMapping("/like/status/{postId}")
    @Operation(summary = "检查是否已点赞")
    public BaseResponse<Boolean> hasLiked(@PathVariable Long postId, HttpServletRequest request) {
        UserVO currentUser = userService.getLoginUser(request);
        Boolean hasLiked = postLikeService.hasLiked(currentUser.getId(), 1, postId);
        return ResultUtils.success(hasLiked);
    }

    /**
     * 收藏/取消收藏帖子
     */
    @PostMapping("/favorite/{postId}")
    @Operation(summary = "收藏/取消收藏帖子")
    public BaseResponse<Boolean> toggleFavorite(@PathVariable Long postId, HttpServletRequest request) {
        UserVO currentUser = userService.getLoginUser(request);
        Boolean result = postFavoriteService.toggleFavorite(currentUser.getId(), postId);
        return ResultUtils.success(result);
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/favorite/status/{postId}")
    @Operation(summary = "检查是否已收藏")
    public BaseResponse<Boolean> hasFavorited(@PathVariable Long postId, HttpServletRequest request) {
        UserVO currentUser = userService.getLoginUser(request);
        Boolean hasFavorited = postFavoriteService.hasFavorited(currentUser.getId(), postId);
        return ResultUtils.success(hasFavorited);
    }

    /**
     * 获取用户收藏的帖子列表
     */
    @GetMapping("/favorite/my")
    @Operation(summary = "获取我的收藏帖子")
    public BaseResponse<Page<PostVO>> getMyFavoritePosts(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        UserVO currentUser = userService.getLoginUser(request);
        Page<PostVO> favoritePage = postFavoriteService.getUserFavoritePosts(currentUser.getId(), current, pageSize);
        return ResultUtils.success(favoritePage);
    }

    /**
     * 获取指定用户收藏的帖子列表
     */
    @GetMapping("/favorite/user/{userId}")
    @Operation(summary = "获取用户收藏的帖子")
    public BaseResponse<Page<PostVO>> getUserFavoritePosts(
            @PathVariable Long userId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<PostVO> favoritePage = postFavoriteService.getUserFavoritePosts(userId, current, pageSize);
        return ResultUtils.success(favoritePage);
    }
}
