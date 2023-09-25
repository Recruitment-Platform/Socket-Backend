create table IF NOT EXISTS recruitskill
(
      recruit_skill_id   bigint    not null  auto_increment    primary key,
      recruit_id         bigint    not null,
      skill_id           bigint    not null,
      FOREIGN KEY (recruit_id) REFERENCES recruit (recruit_id),
      FOREIGN KEY (skill_id) REFERENCES skill (skill_id)
);