package com.project.socket.post.service;

import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostSaveInfo;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostSaveService {

  private final PostJpaRepository postJpaRepository;
  private final UserJpaRepository userJpaRepository;

  /**
   * 포스트(게시물) 생성
   */
  @Transactional
  public Post createPost(PostSaveInfo postSaveInfo) {
    User user = findUser(postSaveInfo.userId());

    Post postToSave = Post.createNewPost(user, postSaveInfo.title(), postSaveInfo.postContent(),
        postSaveInfo.postType(), postSaveInfo.postMeeting());

    return savePost(postToSave);
  }

  private User findUser(Long userId) {
    return userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
  }


  public Post savePost(Post post) {
    return postJpaRepository.save(post);
  }
}
