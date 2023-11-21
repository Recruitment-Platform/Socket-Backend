package com.project.socket.chatroom.repository;

import com.project.socket.chatroom.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
