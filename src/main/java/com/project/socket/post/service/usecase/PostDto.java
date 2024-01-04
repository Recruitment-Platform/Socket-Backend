package com.project.socket.post.service.usecase;

import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostDto {

  private Long postId;
  private String title;
  private String postContent;
  private PostType postType;
  private PostMeeting postMeeting;
  private PostStatus postStatus;
  private LocalDateTime createdAt;
  private Long userId;
  private String userNickname;

  public PostDto(Long postId, String title, String postContent, PostType postType,
      PostMeeting postMeeting, PostStatus postStatus, Long userId, String userNickname,
      LocalDateTime createdAt) {
    this.postId = postId;
    this.title = title;
    this.postContent = postContent;
    this.postType = postType;
    this.postMeeting = postMeeting;
    this.postStatus = postStatus;
    this.createdAt = createdAt;
    this.userId = userId;
    this.userNickname = userNickname;
  }
}
