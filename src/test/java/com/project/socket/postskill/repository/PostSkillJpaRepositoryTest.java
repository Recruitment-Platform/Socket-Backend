package com.project.socket.postskill.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.postskill.model.PostSkill;
import com.project.socket.skill.model.Skill;
import com.project.socket.skill.repository.SkillJpaRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@CustomDataJpaTest
class PostSkillJpaRepositoryTest {

  @Autowired
  PostSkillJpaRepository postSkillJpaRepository;

  @Autowired
  PostJpaRepository postJpaRepository;

  @Autowired
  SkillJpaRepository skillJpaRepository;

  @Sql("savePostSkill.sql")
  @Test
  void PostSkill_엔티티_저장한다() {
    Optional<Post> foundPost = postJpaRepository.findById(1L);
    Optional<Skill> foundSkill = skillJpaRepository.findBySkillName("Java");

    PostSkill postSkill = PostSkill.builder()
        .psPost(foundPost.get())
        .psSkill(foundSkill.get())
        .build();

    PostSkill savedPostSkill = postSkillJpaRepository.save(postSkill);

    assertAll(
        () -> assertThat(savedPostSkill.getId()).isNotNull(),
        () -> assertThat(savedPostSkill.getPsPost()).isNotNull(),
        () -> assertThat(savedPostSkill.getPsSkill()).isNotNull()
    );
  }

  @Sql("findAllPostsBySkillName.sql")
  @Test
  void skillId에_해당하는_postId를_반환한다() {

    List<Long> skillIds = List.of(1L, 2L);

    HashSet<Long> allPostId = postSkillJpaRepository.findPostIdsBySkillIds(skillIds);

    System.out.println(Arrays.toString(allPostId.toArray()));

    assertThat(allPostId.size()).isEqualTo(2);
  }

  @Sql("findAllPostsBySkillName.sql")
  @Test
  void skillId가_존재하지_않을_경우_빈_HashSet을_반환한다() {
    List<Long> skillIds = Collections.emptyList();

    HashSet<Long> allPostId = postSkillJpaRepository.findPostIdsBySkillIds(skillIds);

    assertThat(allPostId).isEmpty();
  }
}