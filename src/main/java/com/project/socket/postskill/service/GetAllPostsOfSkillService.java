package com.project.socket.postskill.service;

import static com.project.socket.post.model.QPost.post;

import com.project.socket.post.exception.UnsupportedSortException;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostDto;
import com.project.socket.postskill.repository.PostSkillJpaRepository;
import com.project.socket.postskill.service.usecase.GetAllPostsOfSkillUseCase;
import com.project.socket.skill.exception.SkillNotFoundException;
import com.project.socket.skill.repository.SkillJpaRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
  private final SkillJpaRepository skillJpaRepository;
  private final PostSkillJpaRepository postSkillJpaRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<PostDto> getPostsUsingSkill(String skillName, Pageable pageable) {
    HashSet<Long> allPostIdBySkill = new HashSet<>();

    if (skillName != null && !skillName.isEmpty()) {
      List<String> hashTags = splitHashTagsUrl(skillName);

      // strings에 있는 skillName의 Id값 list에 저장
      List<Long> skillIdsBySkillName = skillJpaRepository.findAllIdBySkillNames(hashTags);
      if (skillIdsBySkillName.isEmpty()) {
        throw new SkillNotFoundException();
      }

      //skillId와 맵핑된 postId List로 반환. 각각 다른 skillId 2개가 한개의 postId를 가리킬 경우.중복 제거
      allPostIdBySkill = postSkillJpaRepository.findPostIdsBySkillIds(skillIdsBySkillName);

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
    }

    throw new UnsupportedSortException();
  }

  //URL 주소: Java%2CSpring -> Java,Spring -> [Java,Spring]
  private List<String> splitHashTagsUrl(String skillName) {
    String[] hashtagArray = skillName.split(",");

    return new ArrayList<>(List.of(hashtagArray));
  }
}
