package com.project.socket.userchatroom.model;

import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_chat_room")
public class UserChatRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userChatRoomId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;

  @Builder
  public UserChatRoom(Long userChatRoomId, User user, ChatRoom chatRoom) {
    this.userChatRoomId = userChatRoomId;
    this.user = user;
    this.chatRoom = chatRoom;
  }
}
