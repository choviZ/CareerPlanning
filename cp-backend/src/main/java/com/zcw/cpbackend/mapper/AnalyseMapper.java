package com.zcw.cpbackend.mapper;

import com.zcw.cpbackend.model.dto.analyse.ResourceOverviewDTO;
import com.zcw.cpbackend.model.dto.analyse.ResourceTypeDistributionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnalyseMapper {

    @Select("""
            SELECT 
              resource_type AS resourceType,
              CASE resource_type 
                WHEN 1 THEN '技术文档' 
                WHEN 2 THEN '行业动态' 
              END AS typeName,
              COUNT(*) AS typeCount,
              ROUND(COUNT(*) / (SELECT COUNT(*) FROM learning_resource WHERE is_deleted = 0) * 100, 2) AS typeRatio,
              ROUND(AVG(view_count), 2) AS avgView,
              SUM(view_count) AS totalView,
              SUM(CASE WHEN is_top = 1 THEN 1 ELSE 0 END) AS topCount
            FROM learning_resource
            WHERE is_deleted = 0
            GROUP BY resource_type
            ORDER BY typeCount DESC
            """)
    List<ResourceTypeDistributionDTO> calculateTypeDistribution();

    @Select("""
        SELECT 
          COUNT(*) AS totalResources,
          SUM(CASE WHEN is_deleted = 0 THEN 1 ELSE 0 END) AS validResources,
          SUM(CASE WHEN is_deleted = 1 THEN 1 ELSE 0 END) AS deletedResources,
          ROUND(AVG(CASE WHEN is_deleted = 0 THEN view_count ELSE NULL END), 2) AS avgViewCount,
          SUM(CASE WHEN is_deleted = 0 THEN view_count ELSE 0 END) AS totalViewCount
        FROM learning_resource
        """)
    ResourceOverviewDTO getResourceOverview();
}
