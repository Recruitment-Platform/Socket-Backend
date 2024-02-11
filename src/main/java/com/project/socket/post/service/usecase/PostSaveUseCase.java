package com.project.socket.post.service.usecase;

import com.project.socket.post.model.Post;
import java.util.List;

public interface PostSaveUseCase {

  Post createPost(PostSaveCommand postSaveInfo);

  void mapPostWithSkill(Post post, List<String> skillNames);
}
