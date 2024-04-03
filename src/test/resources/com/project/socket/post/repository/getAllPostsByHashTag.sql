insert into user (user_id, nickname, social_id, social_provider, role, profile_setup, created_at,
                  modified_at)
values (1, 'nickname', '1234', 'GOOGLE', 'ROLE_USER', false, now(), now());

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (1, 1, 'title1', 'content1', now(), now(), 'CREATED', 'PROJECT');

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (2, 1, 'title2', 'content2', date_add(now(),INTERVAL 1 HOUR), now(), 'MODIFIED', 'PROJECT');

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (3, 1, 'title3', 'content3', date_add(now(),INTERVAL 2 HOUR), now(), 'CREATED', 'STUDY');

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (4, 1, 'title4', 'content4', date_add(now(),INTERVAL 3 HOUR), now(), 'MODIFIED', 'PROJECT');

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (5, 1, 'title5', 'content5', date_add(now(),INTERVAL 4 HOUR), now(), 'CREATED', 'STUDY');


