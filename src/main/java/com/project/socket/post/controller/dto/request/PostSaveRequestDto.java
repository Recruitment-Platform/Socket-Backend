package com.project.socket.post.controller.dto.request;

import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostType;
import com.project.socket.post.service.usecase.PostSaveInfo;
import jakarta.validation.constraints.NotBlank;

public record PostSaveRequestDto(@NotBlank String title, @NotBlank String postContent,
                                 @NotBlank PostType postType, @NotBlank PostMeeting postMeeting) {

    public PostSaveInfo toSave(Long userId) {
        return new PostSaveInfo(title, postContent, postType, postMeeting, userId);
    }


}
