package com.project.socket.post.service;

import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.post.service.usecase.PostSaveCommand;
import com.project.socket.post.service.usecase.PostSaveUseCase;
import com.project.socket.postskill.model.PostSkill;
import com.project.socket.postskill.repository.PostSkillJpaRepository;
import com.project.socket.skill.model.Skill;
import com.project.socket.skill.repository.SkillJpaRepository;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class PostSaveService implements PostSaveUseCase {

  private final PostJpaRepository postJpaRepository;
  private final UserJpaRepository userJpaRepository;

  private final SkillJpaRepository skillJpaRepository;
  private final PostSkillJpaRepository postSkillJpaRepository;

  /**
   * 포스트(게시물) 생성
   */
  @Override
  @Transactional
  public Post createPost(PostSaveCommand postSaveInfo) {
    User user = findUser(postSaveInfo.userId());

    Post postToSave = savePost(postSaveInfo.toEntity(user));

    //태그가 있을 경우 실행
    if (!isEmptyTag(postSaveInfo)) {
      mapPostWithSkill(postToSave, postSaveInfo.skillNames());
    }
    return postToSave;
  }

  //태그와 게시물 맵핑
  @Override
  public void mapPostWithSkill(Post post, List<String> skillNames) {
    // 게시물과 같이 입력된 해시태그(skill)가 존재할 경우 불러 오고, 존재하지 않을 경우 저장
    skillNames.stream()
        .map(hashtag -> findSkillName(hashtag)
            .orElseGet(() -> saveSkillName(hashtag))
        )
        .forEach(hashTag -> mapHashTagToPost(post, hashTag));
  }

  private Optional<Skill> findSkillName(String hashtag) {
    return skillJpaRepository.findBySkillName(hashtag);
  }

  private Skill saveSkillName(String hashtag) {
    Skill skill = Skill.builder().skillName(hashtag).build();
    return skillJpaRepository.save(skill);
  }

  private PostSkill mapHashTagToPost(Post post, Skill hashTag) {
    PostSkill postSkill = PostSkill.builder().psPost(post).psSkill(hashTag).build();
    return postSkillJpaRepository.save(postSkill);
  }

  private boolean isEmptyTag(PostSaveCommand postSaveInfo) {
    return ObjectUtils.isEmpty(postSaveInfo.skillNames());
  }

  private User findUser(Long userId) {
    return userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
  }


  private Post savePost(Post post) {
    return postJpaRepository.save(post);
  }
}
