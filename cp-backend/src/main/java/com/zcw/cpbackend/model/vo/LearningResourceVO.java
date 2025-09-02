package com.zcw.cpbackend.model.vo;

import com.zcw.cpbackend.model.entity.LearningResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学习资源视图对象
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningResourceVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    private Long id;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 文本内容
     */
    private String content;

    /**
     * 资源摘要
     */
    private String summary;

    /**
     * 资源类型：1-技术文档，2-行业动态
     */
    private Integer resourceType;

    /**
     * 资源类型名称
     */
    private String resourceTypeName;

    /**
     * 内容类型：1-markdown
     */
    private Integer contentType;

    /**
     * 内容类型名称
     */
    private String contentTypeName;

    /**
     * 分类名称
     */
    private String category;

    /**
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 发布者ID
     */
    private Long authorId;

    /**
     * 发布者信息
     */
    private UserVO author;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 是否置顶：0-否，1-是
     */
    private Integer isTop;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 对象转包装类
     *
     * @param learningResource 原始对象
     * @return 包装类
     */
    public static LearningResourceVO objToVo(LearningResource learningResource) {
        if (learningResource == null) {
            return null;
        }
        LearningResourceVO learningResourceVO = new LearningResourceVO();
        BeanUtils.copyProperties(learningResource, learningResourceVO);
        
        // 设置资源类型名称
        if (learningResource.getResourceType() != null) {
            switch (learningResource.getResourceType()) {
                case 1:
                    learningResourceVO.setResourceTypeName("技术文档");
                    break;
                case 2:
                    learningResourceVO.setResourceTypeName("行业动态");
                    break;
                default:
                    learningResourceVO.setResourceTypeName("未知类型");
                    break;
            }
        }
        
        // 设置内容类型名称
        if (learningResource.getContentType() != null) {
            switch (learningResource.getContentType()) {
                case 1:
                    learningResourceVO.setContentTypeName("Markdown");
                    break;
                default:
                    learningResourceVO.setContentTypeName("未知格式");
                    break;
            }
        }
        
        return learningResourceVO;
    }

}