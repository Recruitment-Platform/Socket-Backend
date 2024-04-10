package com.project.socket.postskill.service.usecase;

import com.project.socket.post.service.usecase.PostDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllPostsOfSkillUseCase {

  Page<PostDto> getPostsUsingSkill(List<Long> hashtagIds, Pageable pageable);
}
