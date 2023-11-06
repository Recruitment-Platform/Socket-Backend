package com.project.socket.post.service.usecase;

import com.project.socket.post.model.Post;
import org.springframework.transaction.annotation.Transactional;

public interface PostSaveUseCase {

  @Transactional
  Post createPost(PostSaveCommand postSaveInfo);


}
