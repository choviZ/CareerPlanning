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

-- 评测结果表
CREATE TABLE assessment_result
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id          BIGINT      NOT NULL COMMENT '用户ID',
    test_type        VARCHAR(20) NOT NULL COMMENT '测评类型',
    result_code      VARCHAR(20) NOT NULL COMMENT '结果代码（如INTJ、RIA等）',
    dimension_scores JSON        NOT NULL COMMENT '各维度得分详情',
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '完成时间',
    is_deleted        TINYINT DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
    INDEX idx_user_id (user_id),
    INDEX idx_test_type (test_type),
    INDEX idx_result_code (result_code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='测评结果表';

-- 职业表
CREATE TABLE career
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name            VARCHAR(100) NOT NULL COMMENT '职业名称',
    description     TEXT COMMENT '职业描述',
    required_skills TEXT COMMENT '所需技能',
    job_outlook     TEXT COMMENT '就业前景',
    average_salary  VARCHAR(100) COMMENT '平均薪资范围',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='职业表';

-- 测评结果与职业关联表（多对多关系）
CREATE TABLE result_career_mapping
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    test_type           VARCHAR(20) NOT NULL COMMENT '测评类型',
    result_code         VARCHAR(20) NOT NULL COMMENT '测评结果代码',
    career_id           BIGINT      NOT NULL COMMENT '职业ID',
    compatibility_score INT COMMENT '兼容性评分（0-100）',
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_result_career (result_code, career_id, test_type),
    INDEX idx_result_code (result_code),
    INDEX idx_career_id (career_id),
    INDEX idx_test_type (test_type),
    CONSTRAINT fk_mapping_career FOREIGN KEY (career_id) REFERENCES career (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='测评结果职业关联表';