package com.project.socket.post.exception;

import static com.project.socket.common.error.ErrorCode.POST_DELETED_NOT_FOUND;

import com.project.socket.common.error.exception.BusinessException;

public class PostDeletedException extends BusinessException {

  public PostDeletedException(Long postId) {
    super("Post ID: " + postId, POST_DELETED_NOT_FOUND);
  }

}
