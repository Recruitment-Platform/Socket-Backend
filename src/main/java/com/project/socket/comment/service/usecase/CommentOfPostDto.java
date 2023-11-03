package com.project.socket.comment.service.usecase;

import com.project.socket.comment.model.CommentStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentOfPostDto {

  private Long commentId;
  private String content;
  private CommentStatus commentStatus;
  private Long parentId;
  private LocalDateTime createdAt;
  private Long writerId;
  private String writerNickname;

  public CommentOfPostDto(Long commentId, String content, CommentStatus commentStatus,
      Long parentId, LocalDateTime createdAt, Long writerId, String writerNickname) {
    this.commentId = commentId;
    this.content = content;
    this.commentStatus = commentStatus;
    this.parentId = parentId;
    this.createdAt = createdAt;
    this.writerId = writerId;
    this.writerNickname = writerNickname;
  }
}
