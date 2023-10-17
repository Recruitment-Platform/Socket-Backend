package com.project.socket.comment.service.usecase;

public record AddReplyCommand(
    String content,
    Long userId,
    Long postId,
    Long parentId
) {

}
