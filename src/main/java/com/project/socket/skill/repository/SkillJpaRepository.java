package com.project.socket.skill.repository;

import com.project.socket.skill.model.Skill;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillJpaRepository extends JpaRepository<Skill, Long> {

  Optional<Skill> findBySkillName(String skillName);
}
