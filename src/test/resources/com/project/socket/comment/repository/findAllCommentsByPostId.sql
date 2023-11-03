insert into user (user_id, nickname, social_id, social_provider, role, profile_setup, created_at,
                  modified_at)
values (1, 'nickname', '1234', 'GOOGLE', 'ROLE_USER', false, now(), now());

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (1, 1, 'title', 'content', now(), now(), 'CREATED', 'PROJECT');

insert into comment (comment_id, post_id, user_id, content, comment_status, created_at, modified_at,
                     parent_id)
VALUES (1, 1, 1, '1', 'CREATED', now(), now(), null);

insert into comment (comment_id, post_id, user_id, content, comment_status, created_at, modified_at,
                     parent_id)
VALUES (2, 1, 1, '2', 'DELETED', now(), now(), 1);

insert into comment (comment_id, post_id, user_id, content, comment_status, created_at, modified_at,
                     parent_id)
VALUES (3, 1, 1, '3', 'DELETED', now(), now(), 1);
insert into comment (comment_id, post_id, user_id, content, comment_status, created_at, modified_at,
                     parent_id)
VALUES (4, 1, 1, '5', 'DELETED', now(), now(), null);

insert into comment (comment_id, post_id, user_id, content, comment_status, created_at, modified_at,
                     parent_id)
VALUES (5, 1, 1, '4', 'CREATED', now(), now(), 1);

insert into comment (comment_id, post_id, user_id, content, comment_status, created_at, modified_at,
                     parent_id)
VALUES (6, 1, 1, '6', 'CREATED', now(), now(), 4);

insert into comment (comment_id, post_id, user_id, content, comment_status, created_at, modified_at,
                     parent_id)
VALUES (7, 1, 1, '7', 'CREATED', now(), now(), 4);

insert into comment (comment_id, post_id, user_id, content, comment_status, created_at, modified_at,
                     parent_id)
VALUES (8, 1, 1, '8', 'DELETED', now(), now(), null);