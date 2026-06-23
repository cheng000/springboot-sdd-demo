-- =====================================================
-- 用户表
-- =====================================================
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id          BIGSERIAL       PRIMARY KEY,
    username    VARCHAR(32)     NOT NULL,
    email       VARCHAR(128)    NOT NULL,
    phone       VARCHAR(20)     DEFAULT NULL,
    age         INTEGER         DEFAULT NULL,
    status      SMALLINT        NOT NULL DEFAULT 1,
    create_time TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 用户名唯一索引，防止并发场景下重复
CREATE UNIQUE INDEX uk_t_user_username ON t_user (username);

-- 初始化示例数据
INSERT INTO t_user (username, email, phone, age, status) VALUES
    ('alice',  'alice@example.com',  '13800000001', 25, 1),
    ('bob',    'bob@example.com',    '13800000002', 30, 1),
    ('charlie','charlie@example.com','13800000003', 28, 0);
