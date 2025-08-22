package com.zcw.cpbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.zcw.cpbackend.exception.BusinessException;
import com.zcw.cpbackend.exception.ErrorCode;
import com.zcw.cpbackend.model.dto.user.UserAddRequest;
import com.zcw.cpbackend.model.dto.user.UserLoginRequest;
import com.zcw.cpbackend.model.dto.user.UserQueryRequest;
import com.zcw.cpbackend.model.dto.user.UserRegisterRequest;
import com.zcw.cpbackend.model.dto.user.UserUpdateRequest;
import com.zcw.cpbackend.model.vo.UserLoginVO;
import com.zcw.cpbackend.model.vo.UserVO;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zcw.cpbackend.model.entity.User;
import com.zcw.cpbackend.mapper.UserMapper;
import com.zcw.cpbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户表 服务层实现。
 *
 * @author zcw
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String SALT = "cp";

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码不同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 账号不能包含特殊字符
        if (!userAccount.matches("^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
        // 2. 账号不能重复
        long count = this.count(QueryWrapper.create().eq(User::getUserAccount, userAccount));
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        // 3. 密码加密
        String encryptPassword = DigestUtil.md5Hex(userPassword + SALT);
        // 4. 新增用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserName("小黑子" + RandomUtil.randomString(6));
        user.setUserPassword(encryptPassword);
        user.setUserRole("user");
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @Override
    public UserLoginVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 账号不能包含特殊字符
        if (!userAccount.matches("^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }

        // 2. 密码加密
        String encryptPassword = DigestUtil.md5Hex(userPassword + SALT);

        // 查询用户是否存在
        QueryWrapper queryWrapper = QueryWrapper.create().eq(User::getUserAccount, userAccount);
        User user = this.getOne(queryWrapper);
        // 账号不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
        }
        // 密码不正确
        if (!user.getUserPassword().equals(encryptPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
        }

        // 3. 登录成功，Sa-Token 登录
        StpUtil.login(user.getId());

        // 4. 返回脱敏后的用户信息和token
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setId(user.getId());
        userLoginVO.setToken(StpUtil.getTokenValue());
        userLoginVO.setUserAccount(user.getUserAccount());
        userLoginVO.setUserName(user.getUserName());
        userLoginVO.setUserAvatar(user.getUserAvatar());
        userLoginVO.setUserProfile(user.getUserProfile());
        userLoginVO.setUserRole(user.getUserRole());

        return userLoginVO;
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public UserVO getLoginUser(HttpServletRequest request) {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (loginId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = Long.valueOf(loginId.toString());
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return UserVO.objToVo(user);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        StpUtil.logout();
        return true;
    }

    /**
     * 添加用户
     *
     * @param userAddRequest
     * @return
     */
    @Override
    public boolean addUser(UserAddRequest userAddRequest) {
        // 1. 校验
        String userAccount = userAddRequest.getUserAccount();
        String userPassword = userAddRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 账号不能包含特殊字符
        if (!userAccount.matches("^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }

        // 2. 账号不能重复
        QueryWrapper queryWrapper = QueryWrapper.create().eq(User::getUserAccount, userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }

        // 3. 密码加密
        String encryptPassword = DigestUtil.md5Hex(userPassword + SALT);

        // 4. 新增用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(userAddRequest.getUserName());
        user.setUserAvatar(userAddRequest.getUserAvatar());
        user.setUserProfile(userAddRequest.getUserProfile());
        user.setUserRole(userAddRequest.getUserRole());
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        return this.save(user);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return this.removeById(id);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @return
     */
    @Override
    public boolean updateUser(UserUpdateRequest userUpdateRequest) {
        // 1. 校验
        Long id = userUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 查询用户是否存在
        User oldUser = this.getById(id);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 3. 更新用户
        User user = new User();
        user.setId(id);
        user.setUserName(userUpdateRequest.getUserName());
        user.setUserAvatar(userUpdateRequest.getUserAvatar());
        user.setUserProfile(userUpdateRequest.getUserProfile());
        user.setUserRole(userUpdateRequest.getUserRole());
        if (StringUtils.isNotBlank(userUpdateRequest.getUserPassword())) {
            user.setUserPassword(DigestUtil.md5Hex(userUpdateRequest.getUserPassword() + SALT));
        }
        user.setUpdateAt(LocalDateTime.now());
        return this.updateById(user);
    }

    /**
     * 分页查询用户
     *
     * @param userQueryRequest
     * @return
     */
    @Override
    public Page<User> queryUser(UserQueryRequest userQueryRequest) {
        // 1. 构建查询条件
        QueryWrapper queryWrapper = getQueryWrapper(userQueryRequest);
        int current = userQueryRequest.getCurrent();
        int pageSize = userQueryRequest.getPageSize();
        // 2. 分页查询
        return page(Page.of(current, pageSize), queryWrapper);
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @Override
    public UserVO queryUserById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = this.getById(id);
        return UserVO.objToVo(user);
    }

    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        // 构建
        return QueryWrapper.create()
                .eq(User::getId, id, ObjectUtils.isNotEmpty(id))
                .eq(User::getUserAccount, userAccount, StrUtil.isNotBlank(userAccount))
                .eq(User::getUserName, userName, StrUtil.isNotBlank(userName))
                .eq(User::getUserRole, userRole, StrUtil.isNotBlank(userRole))
                .orderBy(sortField, sortOrder);
    }
}
