CREATE DATABASE IF NOT EXISTS cp;

use cp;

-- 用户表
create table if not exists user
(
    id            bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)                           not null comment '账号',
    user_password varchar(512)                           not null comment '密码',
    user_name     varchar(256)                           null comment '用户昵称',
    user_avatar   varchar(1024)                          null comment '用户头像',
    user_profile  varchar(512)                           null comment '用户简介',
    user_role     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    edit_time     datetime     default CURRENT_TIMESTAMP not null comment '编辑时间',
    create_at     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted    tinyint      default 0                 not null comment '是否删除',
    UNIQUE KEY uk_userAccount (user_account),
    INDEX idx_userName (user_name)
) ENGINE = InnoDB
  DEFAULT CHAR SET utf8mb4
  collate = utf8mb4_unicode_ci comment '用户表';

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

-- 用户评估记录表
CREATE TABLE user_assessment
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    test_type          VARCHAR(20)  NOT NULL COMMENT '评估类型',
    result_code        VARCHAR(20)  NOT NULL COMMENT '结果代码',
    result_name        VARCHAR(128) COMMENT '测评结果名称',
    result_desc        TEXT COMMENT '测评结果描述',
    result_icon        VARCHAR(256) COMMENT '图标',
    choices            VARCHAR(128) COMMENT '用户选择的选项列表',
    dimension_scores   TEXT COMMENT '各维度的得分-Json',
    user_id            BIGINT       NOT NULL COMMENT '用户ID',
    career_id          BIGINT COMMENT '职业ID',
    career_name        VARCHAR(128) NOT NULL COMMENT '职业名称',
    career_description TEXT COMMENT '职业描述',
    created_at         DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_deleted         TINYINT  DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
    INDEX idx_user_id (user_id),
    INDEX idx_test_type (test_type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户评估记录表';

-- 评估结果表
CREATE TABLE assessment_result
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    test_type   VARCHAR(20) NOT NULL COMMENT '测评类型',
    result_code VARCHAR(20) NOT NULL COMMENT '结果代码（如INTJ、RIA等）',
    result_name VARCHAR(100) COMMENT '测评结果名称',
    result_desc TEXT COMMENT '测评结果描述',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_deleted  TINYINT  DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
    INDEX idx_test_type (test_type)
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
    is_deleted      TINYINT  DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
    INDEX idx_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='职业表';

-- 评估结果与职业关联表（多对多关系）
CREATE TABLE result_career_mapping
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    result_id           BIGINT       NOT NULL COMMENT '测评结果ID',
    test_type           VARCHAR(20)  NOT NULL COMMENT '测评类型',
    result_code         VARCHAR(20)  NOT NULL COMMENT '测评结果代码',
    career_id           BIGINT       NOT NULL COMMENT '职业ID',
    career_name         VARCHAR(100) NOT NULL COMMENT '职业名称',
    description         TEXT COMMENT '职业描述',
    compatibility_score INT COMMENT '兼容性评分（0-100）',
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_deleted          TINYINT  DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
    UNIQUE KEY uk_result_career (result_code, career_id, test_type),
    INDEX idx_result_code (result_code),
    INDEX idx_career_id (career_id),
    INDEX idx_test_type (test_type),
    CONSTRAINT fk_mapping_career FOREIGN KEY (career_id) REFERENCES career (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='测评结果职业关联表';

-- 简历表
CREATE TABLE resume
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id       BIGINT       NOT NULL COMMENT '用户ID',
    title         VARCHAR(100) NOT NULL COMMENT '简历标题',
    template_id   BIGINT COMMENT '模板ID',
    content       JSON COMMENT '简历内容（JSON格式存储）',
    name          VARCHAR(50) COMMENT '姓名（冗余字段，便于查询）',
    job_intention VARCHAR(100) COMMENT '求职意向（冗余字段，便于查询）',
    status        TINYINT  DEFAULT 1 COMMENT '状态：1-草稿，2-已完成，3-已发布',
    share_code    VARCHAR(32) COMMENT '分享码',
    is_public     TINYINT  DEFAULT 0 COMMENT '是否公开：0-私有，1-公开',
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted    TINYINT  DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_share_code (share_code),
    INDEX idx_name (name),
    INDEX idx_job_intention (job_intention),
    CONSTRAINT fk_resume_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='简历表';

-- 简历模板表
CREATE TABLE resume_template
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name            VARCHAR(100) NOT NULL COMMENT '模板名称',
    description     TEXT COMMENT '模板描述',
    preview_url     VARCHAR(255) COMMENT '预览图URL',
    template_config JSON COMMENT '模板配置（样式、布局等）',
    default_content JSON COMMENT '默认内容结构',
    is_active       TINYINT  DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
    sort_order      INT      DEFAULT 0 COMMENT '排序',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted      TINYINT  DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
    INDEX idx_is_active (is_active),
    INDEX idx_sort_order (sort_order)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='简历模板表';

-- 简历版本表
CREATE TABLE resume_version
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    resume_id      BIGINT NOT NULL COMMENT '简历ID',
    version_number INT    NOT NULL COMMENT '版本号',
    content        JSON COMMENT '版本内容快照',
    remark         VARCHAR(200) COMMENT '版本备注',
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_resume_id (resume_id),
    INDEX idx_version_number (version_number),
    CONSTRAINT fk_version_resume FOREIGN KEY (resume_id) REFERENCES resume (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='简历版本表';

-- 帖子表
CREATE TABLE post
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
    user_id         BIGINT       NOT NULL COMMENT '发帖用户ID',
    title           VARCHAR(200) NOT NULL COMMENT '帖子标题',
    content         LONGTEXT     NOT NULL COMMENT '帖子内容',
    tags            VARCHAR(500) COMMENT 'Json数组',
    view_count      INT      DEFAULT 0 COMMENT '浏览次数',
    like_count      INT      DEFAULT 0 COMMENT '点赞数',
    comment_count   INT      DEFAULT 0 COMMENT '评论数',
    is_essence      TINYINT  DEFAULT 0 COMMENT '是否精华：0-否，1-是',
    status          TINYINT  DEFAULT 1 COMMENT '0-正常，1-待审核',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_comment_at DATETIME COMMENT '最后评论时间',
    is_deleted      TINYINT  DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
    INDEX idx_status_essence (status, is_essence),
    INDEX idx_user_status (user_id, status),
    INDEX idx_created_at (created_at),
    INDEX idx_last_comment_at (last_comment_at),
    FULLTEXT INDEX ft_title_content (title, content)
) COMMENT '帖子表';

-- 用户点赞表（硬删除）
CREATE TABLE forum_like
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
    user_id     BIGINT  NOT NULL COMMENT '用户ID',
    target_type TINYINT NOT NULL COMMENT '点赞目标类型：1-帖子，2-评论',
    target_id   BIGINT  NOT NULL COMMENT '目标ID（帖子ID或评论ID）',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_target (user_id, target_type, target_id),
    INDEX idx_target (target_type, target_id),
    INDEX idx_user_created (user_id, created_at)
) COMMENT '用户点赞表';

-- 用户收藏表（硬删除）
CREATE TABLE forum_favorite
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id    BIGINT NOT NULL COMMENT '用户ID',
    post_id    BIGINT NOT NULL COMMENT '帖子ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_post (user_id, post_id),
    INDEX idx_user_created (user_id, created_at)
) COMMENT '用户收藏表';

-- 3. 评论表（支持层级关系）
CREATE TABLE comment
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    post_id          BIGINT NOT NULL COMMENT '所属帖子ID',
    user_id          BIGINT NOT NULL COMMENT '评论用户ID',
    parent_id        BIGINT   DEFAULT NULL COMMENT '父评论ID，NULL表示顶级评论',
    root_id          BIGINT   DEFAULT NULL COMMENT '根评论ID，用于快速查找评论树',
    reply_to_user_id BIGINT   DEFAULT NULL COMMENT '回复的用户ID',
    content          TEXT   NOT NULL COMMENT '评论内容',
    like_count       INT      DEFAULT 0 COMMENT '点赞数',
    reply_count      INT      DEFAULT 0 COMMENT '回复数',
    level            INT      DEFAULT 1 COMMENT '评论层级：1-顶级评论，2-二级评论，以此类推',
    path             VARCHAR(1000) COMMENT '评论路径，如：1/23/456，便于查询评论树',
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_deleted      TINYINT  DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
    INDEX idx_post_status_created (post_id, is_deleted, created_at),
    INDEX idx_parent_status (parent_id, is_deleted),
    INDEX idx_root_status (root_id, is_deleted),
    INDEX idx_user_status (user_id, is_deleted),
    INDEX idx_path (path),
    FULLTEXT INDEX ft_content (content)
) COMMENT '评论表';