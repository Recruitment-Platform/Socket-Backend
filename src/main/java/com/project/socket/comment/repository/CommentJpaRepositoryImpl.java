package com.project.socket.comment.repository;

import static com.project.socket.comment.model.QComment.comment;
import static com.project.socket.user.model.QUser.user;

import com.project.socket.comment.model.CommentStatus;
import com.project.socket.comment.service.usecase.CommentOfPostDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CommentJpaRepositoryImpl implements CommentJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
  private static final String DELETED_CONTENT = "삭제된 댓글입니다";

  @Transactional(readOnly = true)
  @Override
  public List<CommentOfPostDto> findAllCommentsByPostId(Long postId) {
    return jpaQueryFactory
        .select(Projections.fields(CommentOfPostDto.class,
            comment.id.as("commentId"),
            comment.commentStatus
                .when(CommentStatus.DELETED).then(DELETED_CONTENT)
                .otherwise(comment.content).as("content"),
            comment.commentStatus,
            comment.parentComment.id.as("parentId"),
            comment.createdAt,
            comment.writer.userId.as("writerId"),
            comment.writer.nickname.as("writerNickname")))
        .from(comment)
        .join(comment.writer, user)
        .where(eqPostId(postId))
        .orderBy(comment.parentComment.id.asc())
        .fetch();
  }

  private BooleanExpression eqPostId(Long postId) {
    return postId != null ? comment.cPost.id.eq(postId) : null;
  }
}
