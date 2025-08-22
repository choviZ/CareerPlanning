package com.zcw.cpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mybatisflex.core.paginate.Page;
import com.zcw.cpbackend.common.BaseResponse;
import com.zcw.cpbackend.common.ResultUtils;
import com.zcw.cpbackend.model.dto.user.UserAddRequest;
import com.zcw.cpbackend.model.dto.user.UserLoginRequest;
import com.zcw.cpbackend.model.dto.user.UserQueryRequest;
import com.zcw.cpbackend.model.dto.user.UserRegisterRequest;
import com.zcw.cpbackend.model.dto.user.UserUpdateRequest;
import com.zcw.cpbackend.model.entity.User;
import com.zcw.cpbackend.model.vo.UserLoginVO;
import com.zcw.cpbackend.model.vo.UserVO;
import com.zcw.cpbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        long result = userService.userRegister(userRegisterRequest);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        UserLoginVO userLoginVO = userService.userLogin(userLoginRequest, request);
        return ResultUtils.success(userLoginVO);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/loginUser")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        UserVO userVO = userService.getLoginUser(request);
        return ResultUtils.success(userVO);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    @SaCheckLogin
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 添加用户
     *
     * @param userAddRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addUser(@RequestBody UserAddRequest userAddRequest) {
        boolean result = userService.addUser(userAddRequest);
        return ResultUtils.success(result);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteUser(@PathVariable Long id) {
        boolean result = userService.deleteUser(id);
        return ResultUtils.success(result);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        boolean result = userService.updateUser(userUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询用户
     *
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        Page<User> userPage = userService.queryUser(userQueryRequest);
        return ResultUtils.success(userPage);
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public BaseResponse<UserVO> getUserById(@PathVariable Long id) {
        UserVO userVO = userService.queryUserById(id);
        return ResultUtils.success(userVO);
    }
}
