package com.project.socket.comment.controller.dto.response;

import com.project.socket.comment.model.CommentStatus;
import com.project.socket.comment.service.usecase.CommentOfPostDto;
import java.time.LocalDateTime;
import java.util.List;

public record CommentOfPostResponseDto(
    Long commentId,
    String content,
    CommentStatus commentStatus,
    LocalDateTime createdAt,
    Long writerId,
    String writerNickname,
    List<ChildComment> childComments
) {

  public record ChildComment(
      Long commentId,
      String content,
      CommentStatus commentStatus,
      LocalDateTime createdAt,
      Long writerId,
      String writerNickname
  ) {

  }

  public static CommentOfPostResponseDto toResponse(List<CommentOfPostDto> commentOfPostDtos) {
    CommentOfPostDto parentComment = commentOfPostDtos.get(0);
    List<ChildComment> childComments = commentOfPostDtos.stream()
                                                        .skip(1)
                                                        .map(dto -> new ChildComment(
                                                            dto.getCommentId(),
                                                            dto.getContent(),
                                                            dto.getCommentStatus(),
                                                            dto.getCreatedAt(),
                                                            dto.getWriterId(),
                                                            dto.getWriterNickname()))
                                                        .toList();

    return new CommentOfPostResponseDto(
        parentComment.getCommentId(),
        parentComment.getContent(),
        parentComment.getCommentStatus(),
        parentComment.getCreatedAt(),
        parentComment.getWriterId(),
        parentComment.getWriterNickname(),
        childComments
    );
  }
}
