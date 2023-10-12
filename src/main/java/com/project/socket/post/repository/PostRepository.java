package com.project.socket.post.repository;

import com.project.socket.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  //List<Post> findAllByOrderByCreatedAtDesc();
}
