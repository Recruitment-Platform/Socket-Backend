package com.project.socket.userchatroom.model;

import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;

  @Enumerated(EnumType.STRING)
  private UserChatRoomStatus userChatRoomStatus;

  @Column
  private short unreadCount;

  public void enter() {
    if (isExit()) {
      this.userChatRoomStatus = UserChatRoomStatus.ENTER;
    }
  }

  public boolean isExit() {
    return this.userChatRoomStatus.equals(UserChatRoomStatus.EXIT);
  }

  @Builder
  public UserChatRoom(Long userChatRoomId, User user, ChatRoom chatRoom,
      UserChatRoomStatus userChatRoomStatus, short unreadCount) {
    this.userChatRoomId = userChatRoomId;
    this.user = user;
    this.chatRoom = chatRoom;
    this.userChatRoomStatus = userChatRoomStatus;
    this.unreadCount = unreadCount;
  }
}
