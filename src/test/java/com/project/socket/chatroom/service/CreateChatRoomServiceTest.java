package com.project.socket.chatroom.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.socket.chatroom.exception.WriterCanNotStartChatException;
import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.chatroom.repository.ChatRoomRepository;
import com.project.socket.chatroom.service.usecase.CreateChatRoomCommand;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.user.model.User;
import com.project.socket.userchatroom.model.UserChatRoom;
import com.project.socket.userchatroom.repository.UserChatRoomRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class CreateChatRoomServiceTest {

  @Mock
  PostJpaRepository postJpaRepository;

  @Mock
  ChatRoomRepository chatRoomRepository;

  @Mock
  UserChatRoomRepository userChatRoomRepository;

  @InjectMocks
  CreateChatRoomService createChatRoomService;

  CreateChatRoomCommand command = new CreateChatRoomCommand(1L, 1L);

  @Test
  void postId에_해당하는_포스트가_없으면_PostNotFoundException_예외가_발생한다() {
    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertAll(
        () -> assertThatThrownBy(() -> createChatRoomService.apply(command))
            .isInstanceOf(PostNotFoundException.class),
        () -> verify(chatRoomRepository, never()).save(any()),
        () -> verify(userChatRoomRepository, never()).save(any()),
        () -> verify(userChatRoomRepository, never()).save(any())
    );
  }

  @Test
  void 자신의_포스트에_채팅방을_생성하면_WriterCanNotStartChatException_예외가_발생한다() {
    Post post = Post.builder().user(User.builder().userId(1L).build()).build();

    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.of(post));

    assertAll(
        () -> assertThatThrownBy(() -> createChatRoomService.apply(command))
            .isInstanceOf(WriterCanNotStartChatException.class),
        () -> verify(chatRoomRepository, never()).save(any()),
        () -> verify(userChatRoomRepository, never()).save(any()),
        () -> verify(userChatRoomRepository, never()).save(any())
    );
  }

  @Test
  void 검증이_통과되면_성공적으로_채팅방을_생성하고_반환한다() {
    User applicant = User.builder().userId(1L).build();
    User writer = User.builder().userId(2L).build();
    Post post = Post.builder().user(writer).build();
    ChatRoom savedChatRoom = ChatRoom.builder().chatRoomId(1L).post(post).build();
    UserChatRoom applicantChatRoom = UserChatRoom.builder().userChatRoomId(1L)
                                                 .chatRoom(savedChatRoom)
                                                 .user(applicant).build();

    UserChatRoom writerChatRoom = UserChatRoom.builder().userChatRoomId(2L)
                                              .chatRoom(savedChatRoom)
                                              .user(writer).build();

    when(postJpaRepository.findById(anyLong())).thenReturn(Optional.of(post));
    when(chatRoomRepository.save(any())).thenReturn(savedChatRoom);

    // save 메서드가 2번 실행되기 때문에 thenReturn에 리턴값에 원하는 순서대로 2개의 인자
    when(userChatRoomRepository.save(any())).thenReturn(writerChatRoom, applicantChatRoom);

    ChatRoom chatRoom = createChatRoomService.apply(command);

    assertThat(chatRoom.getChatRoomId()).isEqualTo(1L);
  }
}