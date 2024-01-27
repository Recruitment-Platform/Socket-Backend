package com.project.socket.post.service.usecase;

import com.project.socket.post.model.Post;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface PostSaveUseCase {

  @Transactional
  Post createPost(PostSaveCommand postSaveInfo);

  @Transactional
  void mapPostWithSkill(Post post, List<String> skillNames);
}
