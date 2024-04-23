package com.project.socket.skill.repository;

import java.util.List;

public interface SkillJpaRepositoryCustom {

  List<Long> findAllIdBySkillNames(List<String> skillNames);
}
