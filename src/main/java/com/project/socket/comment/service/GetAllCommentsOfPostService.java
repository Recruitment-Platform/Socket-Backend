package com.project.socket.comment.service;

import com.project.socket.comment.model.CommentStatus;
import com.project.socket.comment.repository.CommentJpaRepository;
import com.project.socket.comment.service.usecase.CommentOfPostDto;
import com.project.socket.comment.service.usecase.GetAllCommentsOfPostUseCase;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetAllCommentsOfPostService implements GetAllCommentsOfPostUseCase {

  private final CommentJpaRepository commentJpaRepository;

  @Override
  @Transactional(readOnly = true)
  public Map<Long, List<CommentOfPostDto>> apply(Long postId) {
    List<CommentOfPostDto> allCommentsByPostId = commentJpaRepository
        .findAllCommentsByPostId(postId);

    Map<Long, List<CommentOfPostDto>> commentsMap = covertToMap(allCommentsByPostId);

    removeAllDeletedGroup(commentsMap);

    return commentsMap;
  }

  // 순서 보장을 위해 LinkedHashMap 사용
  private Map<Long, List<CommentOfPostDto>> covertToMap(List<CommentOfPostDto> comments) {
    LinkedHashMap<Long, List<CommentOfPostDto>> linkedHashMap = new LinkedHashMap<>();

    comments.forEach(commentOfPost -> {
      if (Objects.isNull(commentOfPost.getParentId())) {
        ArrayList<CommentOfPostDto> commentOfPostDtos = new ArrayList<>();
        commentOfPostDtos.add(commentOfPost);
        linkedHashMap.put(commentOfPost.getCommentId(), commentOfPostDtos);
      } else {
        linkedHashMap.get(commentOfPost.getParentId()).add(commentOfPost);
      }
    });

    return linkedHashMap;
  }

  // 그룹의 댓글들이 모두 삭제된 상태면 그룹 제거
  private void removeAllDeletedGroup(Map<Long, List<CommentOfPostDto>> commentsMap) {
    commentsMap.entrySet().removeIf(entry -> areAllCommentsDeleted(entry.getValue()));
  }

  private boolean areAllCommentsDeleted(List<CommentOfPostDto> comments) {
    return comments.stream()
                   .allMatch(comment -> comment.getCommentStatus().equals(CommentStatus.DELETED));
  }
}
