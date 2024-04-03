insert into user (user_id, nickname, social_id, social_provider, role, profile_setup, created_at,
                  modified_at)
values (1, 'nickname', '1234', 'GOOGLE', 'ROLE_USER', false, now(), now());

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (1, 1, 'title1', 'content1', now(), now(), 'CREATED', 'PROJECT');

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (2, 1, 'title2', 'content2', now(), now(), 'MODIFIED', 'STUDY');

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (3, 1, 'title3', 'content3', now(), now(), 'CREATED', 'PROJECT');

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (4, 1, 'title4', 'content4', now(), now(), 'CREATED', 'STUDY');

insert into skill (skill_id, skill_name)
VALUES (1, 'Java');

insert into skill (skill_id, skill_name)
VALUES (2, 'Spring');

insert into skill (skill_id, skill_name)
VALUES (3, 'Python');

insert into post_skill (post_skill_id, post_id, skill_id)
VALUES (1, 1, 1);

insert into post_skill (post_skill_id, post_id, skill_id)
VALUES (2, 2, 2);

insert into post_skill (post_skill_id, post_id, skill_id)
VALUES (3, 3, 3);

insert into post_skill (post_skill_id, post_id, skill_id)
VALUES (4, 1, 2);

insert into post_skill (post_skill_id, post_id, skill_id)
VALUES (5, 4, 3);

