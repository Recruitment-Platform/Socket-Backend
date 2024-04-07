insert into user (user_id, nickname, social_id, social_provider, role, profile_setup, created_at,
                  modified_at)
values (1, 'nickname', '1234', 'GOOGLE', 'ROLE_USER', false, now(), now());

insert into user (user_id, nickname, social_id, social_provider, role, profile_setup, created_at,
                  modified_at)
values (2, 'nickname', '1234', 'GOOGLE', 'ROLE_USER', false, now(), now());

insert into post (post_id, user_id, title, content, created_at, modified_at, post_status, post_type)
VALUES (1, 1, 'title', 'content', now(), now(), 'CREATED', 'PROJECT');

insert into chat_room(chat_room_id, post_id)
values (1, 1);

insert into user_chat_room(user_chat_room_id, user_id, chat_room_id)
values (1, 1, 1);

insert into user_chat_room(user_chat_room_id, user_id, chat_room_id)
values (2, 2, 1);

insert into chat_message(chat_message_id, chat_room_id, sender_id, content, read_count, created_at)
values (1, 1, 2, 'hi', 1, now());

insert into chat_message(chat_message_id, chat_room_id, sender_id, content, read_count, created_at)
values (2, 1, 2, 'hi', 1, now());

insert into chat_message(chat_message_id, chat_room_id, sender_id, content, read_count, created_at)
values (3, 1, 2, 'hi', 1, now());

insert into chat_message(chat_message_id, chat_room_id, sender_id, content, read_count, created_at)
values (4, 1, 1, 'hi', 1, now());

insert into chat_message(chat_message_id, chat_room_id, sender_id, content, read_count, created_at)
values (5, 1, 1, 'hi', 1, now());

insert into chat_message(chat_message_id, chat_room_id, sender_id, content, read_count, created_at)
values (6, 1, 1, 'hi', 1, now());