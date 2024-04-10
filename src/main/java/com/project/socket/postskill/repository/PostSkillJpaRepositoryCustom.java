package com.project.socket.postskill.repository;

import java.util.HashSet;
import java.util.List;

public interface PostSkillJpaRepositoryCustom {

  HashSet<Long> findPostIdsBySkillIds(List<Long> skillIdList);
}
