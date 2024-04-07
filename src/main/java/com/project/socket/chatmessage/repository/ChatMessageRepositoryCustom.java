package com.project.socket.chatmessage.repository;

public interface ChatMessageRepositoryCustom {

  long updateAllUnreadMessages(Long chatRoomId, Long userId);
}
