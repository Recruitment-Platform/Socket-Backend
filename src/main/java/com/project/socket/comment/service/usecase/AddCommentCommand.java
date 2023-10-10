package com.project.socket.comment.service.usecase;

public record AddCommentCommand(
    String content,
    Long userId,
    Long postId
) {

}
