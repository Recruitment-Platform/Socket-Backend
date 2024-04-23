package com.project.socket.post.repository;

import static com.project.socket.post.model.QPost.post;
import static com.project.socket.user.model.QUser.user;

import com.project.socket.post.service.usecase.PostDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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
            post.title,
            post.postContent,
            post.postType,
            post.postMeeting,
            post.postStatus,
            post.createdAt,
            post.user.userId,
            post.user.nickname.as("userNickname")))
        .from(post)
        .join(post.user, user)
        .where(eqPostId(postId))
        .fetchOne());
  }

  private BooleanExpression eqPostId(Long postId) {
    return postId != null ? post.id.eq(postId) : null;
  }

  @Transactional(readOnly = true)
  @Override
  public Page<PostDto> getPostsByHashTag(HashSet<Long> idList, Pageable pageable,
      OrderSpecifier<?> orderSpecifier) {

    List<PostDto> content = jpaQueryFactory
        .select(Projections.fields(PostDto.class,
            post.id.as("postId"),
            post.title,
            post.postContent,
            post.postType,
            post.postMeeting,
            post.postStatus,
            post.createdAt,
            post.user.userId,
            post.user.nickname.as("userNickname")))
        .from(post)
        .join(post.user, user)
        .where(isEmptyPostId(idList))
        .orderBy(orderSpecifier)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPAQuery<Long> countQuery = jpaQueryFactory
        .select(post.count())
        .from(post)
        .join(post.user, user)
        .where(isEmptyPostId(idList));

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  private BooleanExpression isEmptyPostId(HashSet<Long> idList) {
    return !idList.isEmpty() ? post.id.in(idList) : null;
  }

}
