package com.zcw.cpbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.zcw.cpbackend.model.dto.UserAddRequest;
import com.zcw.cpbackend.model.dto.UserLoginRequest;
import com.zcw.cpbackend.model.dto.UserQueryRequest;
import com.zcw.cpbackend.model.dto.UserRegisterRequest;
import com.zcw.cpbackend.model.dto.UserUpdateRequest;
import com.zcw.cpbackend.model.entity.User;
import com.zcw.cpbackend.model.vo.UserLoginVO;
import com.zcw.cpbackend.model.vo.UserVO;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户表 服务层。
 *
 * @author zcw
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    UserLoginVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    UserVO getLoginUser(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 添加用户
     *
     * @param userAddRequest
     * @return
     */
    boolean addUser(UserAddRequest userAddRequest);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    boolean deleteUser(Long id);

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @return
     */
    boolean updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 分页查询用户
     *
     * @param userQueryRequest
     * @return
     */
    Page<User> queryUser(UserQueryRequest userQueryRequest);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    UserVO queryUserById(Long id);
}
