package com.project.socket.chatmessage.model;

import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.common.model.BaseCreatedTime;
import com.project.socket.user.model.User;
import jakarta.persistence.Column;
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
@Table(name = "chat_message")
public class ChatMessage extends BaseCreatedTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long chatMessageId;

  @ManyToOne
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;

  @ManyToOne
  @JoinColumn(name = "sender_id")
  private User sender;

  @Column
  private String content;

  @Column
  private int readCount;

  @Builder
  public ChatMessage(Long chatMessageId, ChatRoom chatRoom, User sender, String content,
      int readCount) {
    this.chatMessageId = chatMessageId;
    this.chatRoom = chatRoom;
    this.sender = sender;
    this.content = content;
    this.readCount = readCount;
  }

  public void toRead(){
    this.readCount = 0;
  }
}
