package com.project.socket.post.repository;

import static com.project.socket.post.model.QPost.post;
import static com.project.socket.user.model.QUser.user;

import com.project.socket.post.service.usecase.PostDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class PostJpaRepositoryImpl implements PostJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Transactional(readOnly = true)
  @Override
  public Optional<PostDto> findPostByPostId(Long postId) {
    return Optional.ofNullable(jpaQueryFactory
        .select(Projections.fields(PostDto.class,
            post.id.as("postId"),
            post.title.as("title"),
            post.postContent.as("postContent"),
            post.postType.stringValue().as("postType"),
            post.postMeeting.stringValue().as("postMeeting"),
            post.postStatus,
            post.createdAt,
            post.user.userId.as("userId"),
            post.user.nickname.as("userNickname")))
        .from(post)
        .join(post.user, user)
        .where(eqPostId(postId))
        .fetchOne());
  }

  private BooleanExpression eqPostId(Long postId) {
    return postId != null ? post.id.eq(postId) : null;
  }
}
