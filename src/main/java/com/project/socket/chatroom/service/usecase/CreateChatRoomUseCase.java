package com.project.socket.chatroom.service.usecase;

import com.project.socket.chatroom.model.ChatRoom;
import java.util.function.Function;

public interface CreateChatRoomUseCase extends Function<CreateChatRoomCommand, ChatRoom> {
}
