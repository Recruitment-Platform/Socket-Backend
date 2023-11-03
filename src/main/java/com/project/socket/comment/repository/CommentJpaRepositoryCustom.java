package com.project.socket.comment.repository;

import com.project.socket.comment.service.usecase.CommentOfPostDto;
import java.util.List;

public interface CommentJpaRepositoryCustom {

  List<CommentOfPostDto> findAllCommentsByPostId(Long postId);
}
