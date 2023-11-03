package com.project.socket.comment.service.usecase;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface GetAllCommentsOfPostUseCase extends
    Function<Long, Map<Long, List<CommentOfPostDto>>> {

}
