package com.zcw.cpbackend.model.entity.resume;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 简历基本信息实体类
 *
 * @author zcw
 */
@Data
public class BasicInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    /**
     * 性别：1-男，2-女
     */
    private Integer gender;

    /**
     * 地址
     */
    private String address;

    /**
     * 求职意向
     */
    private String jobIntention;

    /**
     * 自我介绍
     */
    private String selfIntroduction;
}