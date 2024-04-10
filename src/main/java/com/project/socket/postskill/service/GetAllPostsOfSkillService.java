package com.project.socket.postskill.service;

import static com.project.socket.post.model.QPost.post;

import com.project.socket.post.exception.UnsupportedSortException;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostDto;
import com.project.socket.postskill.repository.PostSkillJpaRepository;
import com.project.socket.postskill.service.usecase.GetAllPostsOfSkillUseCase;
import com.project.socket.skill.exception.SkillNotFoundException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAllPostsOfSkillService implements GetAllPostsOfSkillUseCase {

  private final PostJpaRepository postJpaRepository;
  private final PostSkillJpaRepository postSkillJpaRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<PostDto> getPostsUsingSkill(List<Long> skillIdList, Pageable pageable) {
    HashSet<Long> allPostIdBySkill = new HashSet<>();

    if (skillIdList != null && !skillIdList.isEmpty()) {
      //skillId와 맵핑된 postId List로 반환. 각각 다른 skillId 2개가 한개의 postId를 가리킬 경우.중복 제거
      allPostIdBySkill = postSkillJpaRepository.findPostIdsBySkillIds(skillIdList);

      if (allPostIdBySkill.isEmpty()) {
        // 입력받은 해시태그ID와 맵핑된 게시물이 하나도 없을 경우
        throw new SkillNotFoundException();
      }
    }
    OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageable);

    Page<PostDto> postsByHashTag = postJpaRepository.getPostsByHashTag(allPostIdBySkill, pageable,
        orderSpecifier);

    return postsByHashTag;
  }

  private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
    Sort sort = pageable.getSort();
    Sort.Order order = sort.iterator().next(); //첫번째 정렬 조건 가져옴

    switch (order.getProperty()) {
      case "createdAt":
        return new OrderSpecifier<LocalDateTime>(Order.DESC, post.createdAt);
      // 후에 필요할 다른 정렬 조건 추가

      default:
        throw new UnsupportedSortException();
    }
  }
}
