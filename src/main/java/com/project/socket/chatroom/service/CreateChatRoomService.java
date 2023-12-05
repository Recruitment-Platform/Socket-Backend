package com.project.socket.chatroom.service;

import com.project.socket.chatroom.exception.WriterCanNotStartChatException;
import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.chatroom.repository.ChatRoomRepository;
import com.project.socket.chatroom.service.usecase.CreateChatRoomCommand;
import com.project.socket.chatroom.service.usecase.CreateChatRoomUseCase;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.user.model.User;
import com.project.socket.userchatroom.model.UserChatRoom;
import com.project.socket.userchatroom.model.UserChatRoomStatus;
import com.project.socket.userchatroom.repository.UserChatRoomRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateChatRoomService implements CreateChatRoomUseCase {

  private final PostJpaRepository postJpaRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final UserChatRoomRepository userChatRoomRepository;

  @Override
  @Transactional
  public ChatRoom apply(CreateChatRoomCommand command) {
    Post post = findPost(command.postId());

    UserChatRoom userChatRoom = userChatRoomRepository.findByUserIdAndPostId(
        command.applicantId(), command.postId());

    // 이미 채팅방이 존재하면 입장 처리 후 반환
    if (Objects.nonNull(userChatRoom)) {
      userChatRoom.enter();
      return userChatRoom.getChatRoom();
    }

    checkWriterStartChat(command.applicantId(), post.getUser().getUserId());

    ChatRoom chatRoomToSave = ChatRoom.builder().post(post).build();
    ChatRoom savedChatRoom = chatRoomRepository.save(chatRoomToSave);

    UserChatRoom writerChatRoom = UserChatRoom.builder()
                                              .chatRoom(savedChatRoom)
                                              .user(post.getUser())
                                              .userChatRoomStatus(UserChatRoomStatus.ENTER)
                                              .build();

    UserChatRoom applicantChatRoom = UserChatRoom.builder()
                                                 .chatRoom(savedChatRoom)
                                                 .user(User.builder()
                                                           .userId(command.applicantId())
                                                           .build())
                                                 .userChatRoomStatus(UserChatRoomStatus.ENTER)
                                                 .build();

    userChatRoomRepository.save(writerChatRoom);
    userChatRoomRepository.save(applicantChatRoom);

    return savedChatRoom;
  }

  private Post findPost(Long postId) {
    return postJpaRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
  }

  private void checkWriterStartChat(Long applicantId, Long writerId) {
    if (applicantId.equals(writerId)) {
      throw new WriterCanNotStartChatException();
    }
  }
}
