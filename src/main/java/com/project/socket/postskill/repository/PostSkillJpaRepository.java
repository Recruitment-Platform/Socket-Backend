package com.project.socket.postskill.repository;

import com.project.socket.postskill.model.PostSkill;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostSkillJpaRepository extends JpaRepository<PostSkill, Long> {

  @Query(value = "SELECT ps.psPost.id FROM PostSkill ps WHERE ps.psSkill.id in :skillIdList")
  HashSet<Long> findPostIdsBySkillIds(@Param("skillIdList") List<Long> skillIdList);

}
