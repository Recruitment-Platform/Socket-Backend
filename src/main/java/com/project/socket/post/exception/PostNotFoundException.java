package com.project.socket.post.exception;

import static com.project.socket.common.error.ErrorCode.POST_NOT_FOUND;

import com.project.socket.common.error.exception.BusinessException;

public class PostNotFoundException extends BusinessException {

  public PostNotFoundException(Long postId) {
    super("Post ID: " + postId, POST_NOT_FOUND);
  }
}
