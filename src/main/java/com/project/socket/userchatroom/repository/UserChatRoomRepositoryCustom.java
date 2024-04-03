package com.project.socket.userchatroom.repository;

import com.project.socket.userchatroom.model.UserChatRoom;

public interface UserChatRoomRepositoryCustom {

  UserChatRoom findByUserIdAndPostId(Long userId, Long postId);
  void updateUnreadCount(Long userId, Long chatRoomId);
}
