package com.project.socket.post.repository;


import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@CustomDataJpaTest
class PostJpaRepositoryTest {

    @Autowired
    PostJpaRepository postJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Test
    @Sql("findById.sql")
    void id에_해당하는_포스트가_있다면_포스트가_담긴_Optional_객체를_반환한다() {
        Optional<Post> foundPost = postJpaRepository.findById(1L);

        assertThat(foundPost).isPresent();
    }

    @Test
    void id에_해당하는_포스트가_없다면_빈_Optional_객체를_반환한다() {
        Optional<Post> foundPost = postJpaRepository.findById(1L);

        assertThat(foundPost).isNotPresent();
    }


    //@Sql("savePost.sql")
    @Test
    void Post_엔티티_저장한다() {
        Optional<User> user = userJpaRepository.findById(1L);

        Post post = Post.builder()
                .user(user.get())
                .title("aaaa")
                .postContent("안녕하세요")
                .postStatus(PostStatus.CREATED)
                .postType(PostType.PROJECT)
                .postMeeting(PostMeeting.ONLINE)
                .build();

        Post savedPost = postJpaRepository.save(post);

        assertThat(savedPost.getId()).isNotNull();
    }
}

