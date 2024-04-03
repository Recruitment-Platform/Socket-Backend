package com.project.socket.skill.repository;

import com.project.socket.skill.model.Skill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SkillJpaRepository extends JpaRepository<Skill, Long> {

  Optional<Skill> findBySkillName(String skillName);

  @Query("SELECT s.id from Skill s where s.skillName in :skillNames")
  List<Long> findAllIdBySkillNames(@Param("skillNames") List<String> skillNames);
}
