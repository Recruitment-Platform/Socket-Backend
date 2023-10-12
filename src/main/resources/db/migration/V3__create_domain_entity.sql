create table IF NOT EXISTS post
(
    post_id         bigint          not null  auto_increment    primary key,
    user_id         bigint          not null,
    title           varchar(255)    not null,
    content         text            not null,
    created_at      datetime(6)     not null,
    modified_at     datetime(6)     not null,
    post_status     enum       ('CREATED','MODIFIED','DELETED'),
    post_type       enum       ('PROJECT','STUDY'),
    post_meeting    enum       ('ONLINE', 'OFFLINE', 'ON_OFFLINE'),
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);
create table IF NOT EXISTS comment
(
    comment_id        bigint       not null  auto_increment    primary key,
    post_id           bigint       not null,
    user_id           bigint       not null,
    content           text         not null,
    comment_status    enum         ('CREATED', 'MODIFIED', 'DELETED'),
    created_at        datetime(6)  not null,
    modified_at       datetime(6)  not null,
    group_num         integer      not null,
    group_order       integer      not null,
    comment_class     integer      not null,
    FOREIGN KEY (post_id) REFERENCES post (post_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);
create table IF NOT EXISTS skill
(
    skill_id    bigint  not null  auto_increment    primary key,
    skill_name  varchar(255)  not null
);
create table IF NOT EXISTS post_skill
(
    post_skill_id   bigint    not null  auto_increment    primary key,
    post_id         bigint    not null,
    skill_id        bigint    not null,
    FOREIGN KEY (post_id) REFERENCES post (post_id),
    FOREIGN KEY (skill_id) REFERENCES skill (skill_id)
);
create table IF NOT EXISTS recruit
(
    recruit_id    bigint  not null  auto_increment    primary key,
    field_name    varchar(255)    not null
);

create table IF NOT EXISTS post_recruit
(
       post_recruit_id    bigint  not null  auto_increment    primary key,
       post_id            bigint  not null,
       recruit_id         bigint  not null,
       FOREIGN KEY (post_id) REFERENCES post (post_id),
       FOREIGN KEY (recruit_id) REFERENCES recruit (recruit_id)
);
create table IF NOT EXISTS recruit_skill
(
      recruit_skill_id   bigint    not null  auto_increment    primary key,
      recruit_id         bigint    not null,
      skill_id           bigint    not null,
      FOREIGN KEY (recruit_id) REFERENCES recruit (recruit_id),
      FOREIGN KEY (skill_id) REFERENCES skill (skill_id)
);