package com.project.socket.postskill.repository;

import static com.project.socket.postskill.model.QPostSkill.postSkill;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class PostSkillJpaRepositoryImpl implements PostSkillJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Transactional(readOnly = true)
  @Override
  public HashSet<Long> findPostIdsBySkillIds(List<Long> skillIdList) {
    return new HashSet<>(jpaQueryFactory
        .select(postSkill.psPost.id)
        .from(postSkill)
        .where(postSkill.psSkill.id.in(skillIdList))
        .fetch()
    );
  }
}
