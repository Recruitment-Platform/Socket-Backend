package com.project.socket.comment.service.usecase;

public record DeleteCommentCommand(
    Long userId,
    Long postId,
    Long commentId
) {

}
