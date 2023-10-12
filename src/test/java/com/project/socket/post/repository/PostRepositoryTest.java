package com.project.socket.post.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@Rollback(false)
public class PostRepositoryTest {

  @Autowired
  PostRepository postRepository;
  @Autowired
  UserJpaRepository userJpaRepository;


  @Test
  public void 게시물_등록_조회() {
    //given
    User user = new User();

    String title = "안녕하세요";
    String postContent = "socket-backend 입니다.";
    PostStatus postStatus = PostStatus.valueOf("CREATED");
    PostType postType = PostType.valueOf("PROJECT");
    PostMeeting postMeeting = PostMeeting.valueOf("ONLINE");

    postRepository.save(Post.builder()
        .title(title)
        .postContent(postContent)
        .postStatus(postStatus)
        .postType(postType)
        .postMeeting(postMeeting)
        .build());

    //when
    List<Post> postList = postRepository.findAll();

    Post findPost = postList.get(0);

    //then

    assertThat(findPost.getTitle()).isEqualTo(title);
    assertThat(findPost.getPostContent()).isEqualTo(postContent);
    assertThat(findPost.getPostStatus()).isEqualTo(postStatus);
    assertThat(findPost.getPostType()).isEqualTo(postType);
    assertThat(findPost.getPostMeeting()).isEqualTo(postMeeting);

  }
}
