package com.project.socket.comment.service.usecase;

public record ModifyCommentCommand(
    Long userId,
    Long postId,
    Long commentId,
    String content
) {

}
