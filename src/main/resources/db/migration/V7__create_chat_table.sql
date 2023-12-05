CREATE TABLE IF NOT EXISTS chat_room
(
    chat_room_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    post_id      BIGINT NOT NULL,

    FOREIGN KEY (post_id) REFERENCES post (post_id)
);

CREATE TABLE IF NOT EXISTS user_chat_room
(
    user_chat_room_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id           BIGINT NOT NULL,
    chat_room_id      BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (chat_room_id) REFERENCES chat_room (chat_room_id)
);

CREATE TABLE IF NOT EXISTS chat_message
(
    chat_message_id BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    chat_room_id    BIGINT      NOT NULL,
    sender_id       BIGINT      NOT NULL,
    content         TEXT        NOT NULL,
    read_count      INTEGER     NOT NULL,
    created_at      DATETIME(6) NOT NULL,

    FOREIGN KEY (chat_room_id) REFERENCES chat_room (chat_room_id),
    FOREIGN KEY (sender_id) REFERENCES user (user_id)
);