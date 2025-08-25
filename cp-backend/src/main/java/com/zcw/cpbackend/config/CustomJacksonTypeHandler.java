package com.zcw.cpbackend.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcw.cpbackend.model.entity.resume.ResumeContent;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自定义Jackson类型处理器
 * 专门用于处理ResumeContent的JSON序列化和反序列化
 *
 * @author zcw
 */
@Component
@MappedTypes({ResumeContent.class})
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
public class CustomJacksonTypeHandler extends BaseTypeHandler<ResumeContent> {

    private static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        CustomJacksonTypeHandler.objectMapper = objectMapper;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ResumeContent parameter, JdbcType jdbcType) throws SQLException {
        try {
            if (objectMapper != null) {
                ps.setString(i, objectMapper.writeValueAsString(parameter));
            } else {
                throw new SQLException("ObjectMapper is not initialized");
            }
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting ResumeContent to JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public ResumeContent getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return parseJson(json);
    }

    @Override
    public ResumeContent getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return parseJson(json);
    }

    @Override
    public ResumeContent getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return parseJson(json);
    }

    private ResumeContent parseJson(String json) throws SQLException {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            if (objectMapper != null) {
                return objectMapper.readValue(json, ResumeContent.class);
            } else {
                throw new SQLException("ObjectMapper is not initialized");
            }
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting JSON to ResumeContent: " + e.getMessage(), e);
        }
    }
}