package com.project.socket.post.service.usecase;

import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostType;

public record PostModifyCommand(

    Long userId,

    Long postId,

    String title,

    String postContent,

    PostType postType,

    PostMeeting postMeeting
) {

  public Post toModifiedEntity() {
    return Post.builder()
        .title(title)
        .postContent(postContent)
        .postType(postType)
        .postMeeting(postMeeting)
        .build();
  }

}
