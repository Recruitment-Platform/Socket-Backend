insert into user (user_id, nickname, social_id, social_provider, role, profile_setup, created_at,
                  modified_at)
values (1, 'nickname', '1234', 'GOOGLE', 'ROLE_USER', false, now(), now());

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (1, 1, 'title1', 'content1', now(), now(), 'CREATED', 'PROJECT');

