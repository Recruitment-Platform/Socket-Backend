package com.project.socket.post.controller.dto.request;

import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//Post에 추가할 데이터
@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

  private Long id;
  private User user;
  private String title;
  private String postContent;
  private PostStatus postStatus;
  private PostType postType;
  private PostMeeting postMeeting;

  @Builder
  public PostSaveRequestDto(Long id, User user, String title, String postContent,
      PostStatus postStatus, PostType postType, PostMeeting postMeeting) {
    this.id = id;
    this.user = user;
    this.title = title;
    this.postContent = postContent;
    this.postStatus = postStatus;
    this.postType = postType;
    this.postMeeting = postMeeting;
  }


  public Post toEntity() {
    return Post.builder()
        .id(id)
        .user(user)
        .title(title)
        .postContent(postContent)
        .postStatus(postStatus)
        .postType(postType)
        .postMeeting(postMeeting)
        .build();
  }
}
