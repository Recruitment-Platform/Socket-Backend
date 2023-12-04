package com.project.socket.chatmessage.repository;

import com.project.socket.chatmessage.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
