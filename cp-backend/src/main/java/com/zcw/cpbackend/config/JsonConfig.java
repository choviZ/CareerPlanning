package com.zcw.cpbackend.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Spring MVC Json 配置
 */
@JsonComponent
public class JsonConfig {

    /**
     * 添加 Long 转 json 精度丢失的配置
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        
        // 添加 Long 转 json 精度丢失的配置
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        
        // 添加自定义LocalDate反序列化器
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        
        objectMapper.registerModule(module);
        
        // 添加 Java 8 时间类型支持
        objectMapper.registerModule(new JavaTimeModule());
        
        return objectMapper;
    }
    
    /**
     * 自定义LocalDate反序列化器
     * 支持多种日期格式：yyyy-MM-dd 和 ISO 8601格式
     */
    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter[] FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ISO_INSTANT
        };
        
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String dateStr = p.getValueAsString();
            if (dateStr == null || dateStr.trim().isEmpty()) {
                return null;
            }
            
            // 如果是ISO 8601格式，先提取日期部分
            if (dateStr.contains("T")) {
                dateStr = dateStr.substring(0, dateStr.indexOf("T"));
            }
            
            // 尝试多种格式解析
            for (DateTimeFormatter formatter : FORMATTERS) {
                try {
                    return LocalDate.parse(dateStr, formatter);
                } catch (DateTimeParseException e) {
                    // 继续尝试下一个格式
                }
            }
            
            throw new IOException("无法解析日期格式: " + dateStr);
        }
    }
}
