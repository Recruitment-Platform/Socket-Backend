CREATE TABLE IF NOT EXISTS user
(
    user_id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname        VARCHAR(255),
    github_link     VARCHAR(255),
    email           VARCHAR(255),
    social_id       VARCHAR(255) NOT NULL,
    social_provider VARCHAR(255) NOT NULL,
    role            VARCHAR(255) NOT NULL,
    profile_setup   TINYINT(1)   NOT NULL,
    created_at      DATETIME(6)  NOT NULL,
    modified_at     DATETIME(6)  NOT NULL
);