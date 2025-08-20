CREATE DATABASE IF NOT EXISTS cp;

use cp;

-- 评估表
CREATE TABLE assessment_question
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    test_type  VARCHAR(20) NOT NULL COMMENT '测评类型',
    content    TEXT        NOT NULL COMMENT '题目内容，如“你更喜欢独处还是社交？”',
    dimension  VARCHAR(20) COMMENT '维度标识，如 MBTI 的 E/I, S/N, T/F, J/P；霍兰德的 R/I/A/S/E/C',
    options    JSON        NOT NULL COMMENT '选项数组，包含内容和分数规则',
    sort_order INT      DEFAULT 0 COMMENT '题目排序序号',
    status     TINYINT  DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_test_type (test_type),
    INDEX idx_dimension (dimension),
    INDEX idx_sort_order (sort_order)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='测评题目表';
