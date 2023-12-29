package com.project.socket.post.controller.dto.response;

import com.project.socket.post.model.PostStatus;
import com.project.socket.post.service.usecase.PostDto;
import java.time.LocalDateTime;

public record PostResponseDto(
    Long postId,
    String title,
    String postContent,
    String postType,
    String postMeeting,
    PostStatus postStatus,
    Long userId,
    String userNickname,
    LocalDateTime createdAt

) {

  public static PostResponseDto toResponse(PostDto singlePostDto) {
    return new PostResponseDto(
        singlePostDto.getPostId(),
        singlePostDto.getTitle(),
        singlePostDto.getPostContent(),
        singlePostDto.getPostType(),
        singlePostDto.getPostMeeting(),
        singlePostDto.getPostStatus(),
        singlePostDto.getUserId(),
        singlePostDto.getUserNickname(),
        singlePostDto.getCreatedAt()
    );
  }
}
