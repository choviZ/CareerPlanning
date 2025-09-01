package com.zcw.cpbackend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 帖子编辑请求
 *
 * @author zcw
 */
@Data
public class PostEditRequest implements Serializable {

    /**
     * 帖子ID
     */
    @NotNull(message = "帖子ID不能为空")
    private Long id;

    /**
     * 帖子标题
     */
    @NotBlank(message = "帖子标题不能为空")
    @Size(max = 200, message = "帖子标题长度不能超过200个字符")
    private String title;

    /**
     * 帖子内容
     */
    @NotBlank(message = "帖子内容不能为空")
    private String content;

    /**
     * 标签，Json数组格式
     */
    @Size(max = 500, message = "标签长度不能超过500个字符")
    private String tags;

    private static final long serialVersionUID = 1L;
}