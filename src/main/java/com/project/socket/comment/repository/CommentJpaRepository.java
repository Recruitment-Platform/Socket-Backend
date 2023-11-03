package com.project.socket.comment.repository;

import com.project.socket.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long>,
    CommentJpaRepositoryCustom {

}
