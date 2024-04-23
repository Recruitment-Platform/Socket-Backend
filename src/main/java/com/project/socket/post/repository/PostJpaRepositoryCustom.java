package com.project.socket.post.repository;

import com.project.socket.post.service.usecase.PostDto;
import com.querydsl.core.types.OrderSpecifier;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostJpaRepositoryCustom {

  Optional<PostDto> findPostByPostId(Long postId);

  Page<PostDto> getPostsByHashTag(HashSet<Long> idList, Pageable pageable, OrderSpecifier<?> orderSpecifier);
}
