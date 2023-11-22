insert into user (user_id, nickname, social_id, social_provider, role, profile_setup, created_at,
                  modified_at)
values (1, 'nickname', '1234', 'GOOGLE', 'ROLE_USER', false, now(), now());

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (1, 1, 'title', 'content', now(), now(), 'CREATED', 'PROJECT');

insert into chat_room(chat_room_id, post_id)
values (1, 1);
