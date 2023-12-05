package com.project.socket.post.service.usecase;

import com.project.socket.post.model.Post;
import jakarta.transaction.Transactional;

public interface PostModifyUseCase {

  @Transactional
  Post modifyPostNew(PostModifyCommand postModifyCommand);

}
