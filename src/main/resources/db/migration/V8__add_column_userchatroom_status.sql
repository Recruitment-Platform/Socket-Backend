ALTER TABLE user_chat_room
    ADD COLUMN user_chat_room_status ENUM ('ENTER', 'EXIT') NOT NULL DEFAULT 'ENTER';