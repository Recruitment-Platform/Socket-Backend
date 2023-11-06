package com.project.socket.post.service.usecase;

import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.user.model.User;

public record PostSaveCommand(
    String title,
    String postContent,
    PostType postType,
    PostMeeting postMeeting,
    Long userId

) {

  public Post toEntity(User user) {
    return Post.builder()
        .user(user)
        .title(title)
        .postContent(postContent)
        .postType(postType)
        .postMeeting(postMeeting)
        .postStatus(PostStatus.CREATED)
        .build();

  }
}
