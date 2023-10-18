package com.project.socket.post.service.usecase;

import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostType;

public record PostSaveInfo(
        String title,
        String postContent,
        PostType postType,
        PostMeeting postMeeting,
        Long userId

) {
}
