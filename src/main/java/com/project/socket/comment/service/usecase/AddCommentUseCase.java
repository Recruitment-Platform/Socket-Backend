package com.project.socket.comment.service.usecase;

import com.project.socket.comment.model.Comment;
import java.util.function.Function;

public interface AddCommentUseCase extends Function<AddCommentCommand, Comment> {

}
