package com.zcw.cpbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JsonConfig测试类
 * 验证日期时间格式处理是否正常
 *
 * @author zcw
 */
@SpringBootTest
public class JsonConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLocalDateDeserialization() throws Exception {
        // 测试不同格式的日期字符串
        String[] dateStrings = {
            "2022-08-31",                    // 标准格式
            "2022-08-31T16:00:00.000Z",      // ISO 8601格式
            "2022-08-31T16:00:00",           // ISO格式无时区
            "2022-08-31T00:00:00.000+08:00"  // 带时区的ISO格式
        };

        LocalDate expectedDate = LocalDate.of(2022, 8, 31);

        for (String dateString : dateStrings) {
            // 创建包含日期的JSON对象
            Map<String, Object> testData = new HashMap<>();
            testData.put("testDate", dateString);
            
            String json = objectMapper.writeValueAsString(testData);
            System.out.println("测试JSON: " + json);
            
            // 反序列化测试
            TestDateObject result = objectMapper.readValue(json, TestDateObject.class);
            
            assertNotNull(result.getTestDate(), "日期不应为null: " + dateString);
            assertEquals(expectedDate, result.getTestDate(), 
                "日期解析错误，输入: " + dateString + ", 期望: " + expectedDate + ", 实际: " + result.getTestDate());
            
            System.out.println("✓ 成功解析: " + dateString + " -> " + result.getTestDate());
        }
    }

    @Test
    public void testNullAndEmptyDateHandling() throws Exception {
        // 测试null和空字符串处理
        String[] testCases = {
            "{\"testDate\": null}",
            "{\"testDate\": \"\"}",
            "{\"testDate\": \"   \"}"
        };

        for (String json : testCases) {
            TestDateObject result = objectMapper.readValue(json, TestDateObject.class);
            assertNull(result.getTestDate(), "空值应该被正确处理: " + json);
            System.out.println("✓ 空值处理正确: " + json);
        }
    }

    @Test
    public void testInvalidDateFormat() {
        // 测试无效日期格式
        String invalidJson = "{\"testDate\": \"invalid-date\"}";
        
        assertThrows(Exception.class, () -> {
            objectMapper.readValue(invalidJson, TestDateObject.class);
        }, "无效日期格式应该抛出异常");
        
        System.out.println("✓ 无效日期格式正确抛出异常");
    }

    /**
     * 测试用的日期对象
     */
    public static class TestDateObject {
        private LocalDate testDate;

        public LocalDate getTestDate() {
            return testDate;
        }

        public void setTestDate(LocalDate testDate) {
            this.testDate = testDate;
        }
    }
}