package com.project.socket.userchatroom.repository;

import com.project.socket.userchatroom.model.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long>{

}
