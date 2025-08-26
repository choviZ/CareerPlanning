package com.zcw.cpbackend.service;

import com.zcw.cpbackend.model.dto.resume.*;
import com.zcw.cpbackend.model.dto.resumetemplate.ResumeTemplateAddRequest;
import com.zcw.cpbackend.model.vo.ResumeTemplateVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 简历模板服务测试类
 * 演示简历模板的创建和样式配置
 *
 * @author zcw
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional // 测试后自动回滚数据
public class ResumeTemplateServiceTest {

    @Resource
    private ResumeTemplateService resumeTemplateService;

    /**
     * 测试创建简历模板 - 演示完整的模板创建和样式配置过程
     */
    @Test
    public void testCreateResumeTemplateWithStyleConfig() {
        // 1. 创建模板样式配置
        Map<String, Object> templateConfig = createTemplateStyleConfig();
        // 2. 创建默认简历内容
        ResumeContent defaultContent = createDefaultResumeContent();
        // 3. 构建简历模板添加请求
        ResumeTemplateAddRequest addRequest = new ResumeTemplateAddRequest();
        addRequest.setTemplateName("现代简约风格模板");
        addRequest.setTemplateDesc("适合IT行业的现代简约风格简历模板，突出技术能力和项目经验");
        addRequest.setPreviewUrl("https://example.com/preview/modern-template.png");
        addRequest.setTemplateConfig(templateConfig);
        addRequest.setDefaultContent(defaultContent);
        addRequest.setTemplateType(2); // 高级模板
        addRequest.setIsFree(0); // 付费模板
        addRequest.setPrice(2999); // 29.99元
        addRequest.setSortOrder(1);
        addRequest.setStatus(1); // 启用状态
        // 4. 创建模板
        Long templateId = resumeTemplateService.addResumeTemplate(addRequest);
        // 5. 验证创建结果
        assertNotNull(templateId);
        assertTrue(templateId > 0);
        // 6. 查询创建的模板
        ResumeTemplateVo templateVo = resumeTemplateService.queryResumeTemplateById(templateId);
        // 7. 验证模板信息
        assertNotNull(templateVo);
        assertEquals("现代简约风格模板", templateVo.getTemplateName());
        assertEquals(2, templateVo.getTemplateType());
        assertEquals(1, templateVo.getIsActive());
        // 8. 验证样式配置
        assertNotNull(templateVo.getTemplateConfig());
        // 9. 验证默认内容
        assertNotNull(templateVo.getDefaultContent());
        assertEquals("张三", templateVo.getDefaultContent().getBasicInfo().getName());
        System.out.println("=== 简历模板创建成功 ===");
        System.out.println("模板ID: " + templateId);
        System.out.println("模板名称: " + templateVo.getTemplateName());
        System.out.println("模板类型: " + getTemplateTypeName(templateVo.getTemplateType()));
        System.out.println("样式配置: " + templateVo.getTemplateConfig());
    }
    
    /**
     * 创建模板样式配置
     * 这里演示了如何配置简历模板的各种样式参数
     */
    private Map<String, Object> createTemplateStyleConfig() {
        Map<String, Object> config = new HashMap<>();
        
        // 整体布局配置
        Map<String, Object> layout = new HashMap<>();
        layout.put("type", "two-column"); // 双栏布局
        layout.put("leftWidth", "30%"); // 左栏宽度
        layout.put("rightWidth", "70%"); // 右栏宽度
        layout.put("gap", "20px"); // 栏间距
        config.put("layout", layout);
        
        // 颜色主题配置
        Map<String, Object> colors = new HashMap<>();
        colors.put("primary", "#2c3e50"); // 主色调
        colors.put("secondary", "#3498db"); // 辅助色
        colors.put("accent", "#e74c3c"); // 强调色
        colors.put("text", "#2c3e50"); // 文字颜色
        colors.put("background", "#ffffff"); // 背景色
        colors.put("border", "#ecf0f1"); // 边框色
        config.put("colors", colors);
        
        // 字体配置
        Map<String, Object> typography = new HashMap<>();
        typography.put("fontFamily", "'Microsoft YaHei', 'PingFang SC', sans-serif");
        typography.put("fontSize", "14px"); // 基础字号
        typography.put("lineHeight", "1.6"); // 行高
        
        // 标题字体配置
        Map<String, Object> headings = new HashMap<>();
        headings.put("h1", Map.of("fontSize", "24px", "fontWeight", "bold", "color", "#2c3e50"));
        headings.put("h2", Map.of("fontSize", "18px", "fontWeight", "600", "color", "#3498db"));
        headings.put("h3", Map.of("fontSize", "16px", "fontWeight", "600", "color", "#2c3e50"));
        typography.put("headings", headings);
        config.put("typography", typography);
        
        // 间距配置
        Map<String, Object> spacing = new HashMap<>();
        spacing.put("sectionMargin", "20px"); // 模块间距
        spacing.put("itemMargin", "12px"); // 条目间距
        spacing.put("padding", "16px"); // 内边距
        config.put("spacing", spacing);
        
        // 边框和阴影配置
        Map<String, Object> borders = new HashMap<>();
        borders.put("radius", "4px"); // 圆角
        borders.put("width", "1px"); // 边框宽度
        borders.put("style", "solid"); // 边框样式
        borders.put("shadow", "0 2px 4px rgba(0,0,0,0.1)"); // 阴影
        config.put("borders", borders);
        
        // 各模块样式配置
        Map<String, Object> sections = new HashMap<>();
        
        // 基本信息模块样式
        Map<String, Object> basicInfo = new HashMap<>();
        basicInfo.put("position", "left"); // 位置：左栏
        basicInfo.put("backgroundColor", "#f8f9fa");
        basicInfo.put("padding", "20px");
        basicInfo.put("borderRadius", "8px");
        sections.put("basicInfo", basicInfo);
        
        // 教育经历模块样式
        Map<String, Object> education = new HashMap<>();
        education.put("position", "right"); // 位置：右栏
        education.put("showTimeline", true); // 显示时间线
        education.put("timelineColor", "#3498db");
        sections.put("education", education);
        
        // 工作经历模块样式
        Map<String, Object> workExperience = new HashMap<>();
        workExperience.put("position", "right");
        workExperience.put("showTimeline", true);
        workExperience.put("highlightCurrent", true); // 高亮当前工作
        sections.put("workExperience", workExperience);
        
        // 项目经历模块样式
        Map<String, Object> projectExperience = new HashMap<>();
        projectExperience.put("position", "right");
        projectExperience.put("showTags", true); // 显示技术标签
        projectExperience.put("tagStyle", Map.of(
            "backgroundColor", "#e3f2fd",
            "color", "#1976d2",
            "borderRadius", "12px",
            "padding", "4px 8px"
        ));
        sections.put("projectExperience", projectExperience);
        
        // 技能模块样式
        Map<String, Object> skills = new HashMap<>();
        skills.put("position", "left");
        skills.put("displayType", "progress"); // 进度条显示
        skills.put("progressColor", "#3498db");
        skills.put("progressHeight", "8px");
        sections.put("skills", skills);
        
        config.put("sections", sections);
        
        return config;
    }
    
    /**
     * 创建默认简历内容
     * 这里演示了如何创建一个完整的简历内容结构
     */
    private ResumeContent createDefaultResumeContent() {
        ResumeContent content = new ResumeContent();
        
        // 1. 基本信息
        BasicInfo basicInfo = new BasicInfo();
        basicInfo.setName("张三");
        basicInfo.setGender(1); // 1-男，2-女
        basicInfo.setBirthDate(LocalDate.of(1995, 6, 15));
        basicInfo.setPhone("138****8888");
        basicInfo.setEmail("zhangsan@example.com");
        basicInfo.setAddress("北京市朝阳区");
        basicInfo.setJobIntention("Java开发工程师");
        basicInfo.setSelfIntroduction("具有3年Java开发经验，熟悉Spring Boot、MyBatis等主流框架，有良好的编程习惯和团队协作能力。");
        content.setBasicInfo(basicInfo);
        
        // 2. 教育经历
        List<Education> educationList = new ArrayList<>();
        Education education = new Education();
        education.setSchoolName("北京理工大学");
        education.setMajor("计算机科学与技术");
        education.setDegree("本科");
        education.setStartDate(LocalDate.of(2017, 9, 1));
        education.setEndDate(LocalDate.of(2021, 6, 30));
        education.setDescription("主修课程：数据结构、算法设计、数据库原理、软件工程等");
        educationList.add(education);
        content.setEducation(educationList);
        
        // 3. 工作经历
        List<WorkExperience> workList = new ArrayList<>();
        WorkExperience work = new WorkExperience();
        work.setCompanyName("北京科技有限公司");
        work.setPosition("Java开发工程师");
        work.setStartDate(LocalDate.of(2021, 7, 1));
        work.setEndDate(LocalDate.of(2024, 12, 31));
        work.setJobDescription("负责公司核心业务系统的开发和维护，参与系统架构设计，优化系统性能。");
        workList.add(work);
        content.setWorkExperience(workList);
        
        // 4. 项目经历
        List<ProjectExperience> projectList = new ArrayList<>();
        ProjectExperience project = new ProjectExperience();
        project.setProjectName("企业管理系统");
        project.setRole("后端开发负责人");
        project.setStartDate(LocalDate.of(2023, 1, 1));
        project.setEndDate(LocalDate.of(2023, 12, 31));
        project.setDescription("基于Spring Boot + MyBatis + Redis的企业管理系统，支持用户管理、权限控制、数据统计等功能。");
        project.setTechnologies(Arrays.asList("Spring Boot", "MyBatis", "Redis", "MySQL", "Vue.js"));
        projectList.add(project);
        content.setProjectExperience(projectList);
        
        // 5. 技能列表
        List<Skill> skillList = new ArrayList<>();
        skillList.add(new Skill("Java", 90));
        skillList.add(new Skill("Spring Boot", 85));
        skillList.add(new Skill("MyBatis", 80));
        skillList.add(new Skill("MySQL", 75));
        skillList.add(new Skill("Redis", 70));
        content.setSkills(skillList);
        
        return content;
    }
    
    /**
     * 获取模板类型名称
     */
    private String getTemplateTypeName(Integer type) {
        switch (type) {
            case 1: return "基础模板";
            case 2: return "高级模板";
            case 3: return "专业模板";
            default: return "未知类型";
        }
    }
    
    /**
     * 测试获取可用模板列表
     */
    @Test
    public void testGetAvailableTemplates() {
        // 先创建一个模板
        testCreateResumeTemplateWithStyleConfig();
        
        // 获取可用模板列表
        List<ResumeTemplateVo> templates = resumeTemplateService.getAvailableTemplates();
        
        assertNotNull(templates);
        assertFalse(templates.isEmpty());
        
        System.out.println("=== 可用模板列表 ===");
        for (ResumeTemplateVo template : templates) {
            System.out.println(String.format("ID: %d, 名称: %s, 类型: %s, 价格: %.2f元", 
                template.getId(), 
                template.getTemplateName(), 
                getTemplateTypeName(template.getTemplateType())));
        }
    }
}