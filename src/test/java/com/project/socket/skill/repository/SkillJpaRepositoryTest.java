package com.project.socket.skill.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.skill.model.Skill;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@CustomDataJpaTest
class SkillJpaRepositoryTest {

  @Autowired
  SkillJpaRepository skillJpaRepository;

  @Test
  @Sql("findBySkillName.sql")
  void skillName에_해당하는_태그값이_존재하면_skillName이_담긴_Optional_객체를_반환한다() {
    Optional<Skill> foundSkill = skillJpaRepository.findBySkillName("Java");

    assertThat(foundSkill).isPresent();
  }

  @Test
  void skillName에_해당하는_태그값이_없다면_빈_Optional_객체를_반환한다() {
    Optional<Skill> foundSkill = skillJpaRepository.findBySkillName("Java");

    assertThat(foundSkill).isNotPresent();
  }

  @Test
  void Skill_엔티티_저장한다() {
    Skill skill = Skill.builder()
        .skillName("Java")
        .build();

    Skill savedSkill = skillJpaRepository.save(skill);

    assertAll(
        () -> assertThat(savedSkill.getId()).isNotNull(),
        () -> assertThat(savedSkill.getSkillName()).isNotNull()
    );
  }
}