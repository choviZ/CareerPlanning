package com.zcw.cpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.model.dto.PostAddRequest;
import com.zcw.cpbackend.model.dto.PostEditRequest;
import com.zcw.cpbackend.model.dto.PostQueryRequest;
import com.zcw.cpbackend.model.vo.PostVO;
import com.zcw.cpbackend.service.PostService;
import jakarta.annotation.Resource;
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
    public BaseResponse<Page<PostVO>> queryPostPage(@RequestBody PostQueryRequest postQueryRequest) {
        Page<PostVO> postPage = postService.queryPostPage(postQueryRequest);
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
}
