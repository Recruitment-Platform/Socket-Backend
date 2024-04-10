package com.project.socket.skill.repository;

import static com.project.socket.skill.model.QSkill.skill;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class SkillJpaRepositoryImpl implements SkillJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Transactional(readOnly = true)
  @Override
  public List<Long> findAllIdBySkillNames(List<String> skillNames) {
    return jpaQueryFactory
        .select(skill.id)
        .from(skill)
        .where(skill.skillName.in(skillNames))
        .fetch();
  }

}
