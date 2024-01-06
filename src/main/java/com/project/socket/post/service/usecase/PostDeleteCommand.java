package com.project.socket.post.service.usecase;

public record PostDeleteCommand(
    Long userId,
    Long postId
) {

}
