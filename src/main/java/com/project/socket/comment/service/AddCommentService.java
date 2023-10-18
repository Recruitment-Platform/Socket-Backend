package com.project.socket.comment.service;

import com.project.socket.comment.model.Comment;
import com.project.socket.comment.repository.CommentJpaRepository;
import com.project.socket.comment.service.usecase.AddCommentCommand;
import com.project.socket.comment.service.usecase.AddCommentUseCase;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.Post;
import com.project.socket.post.repository.PostJpaRepository;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddCommentService implements AddCommentUseCase {

    private final CommentJpaRepository commentJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PostJpaRepository postJpaRepository;

    @Override
    @Transactional
    public Comment apply(AddCommentCommand addCommentCommand) {
        User writer = findUser(addCommentCommand.userId());
        Post postToAddComment = findPost(addCommentCommand.postId());

        Comment commentToSave = Comment.createNewComment(
                postToAddComment, writer, addCommentCommand.content());

        Comment savedComment = saveComment(commentToSave);

        return savedComment;
    }

    private Comment saveComment(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    private User findUser(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Post findPost(Long postId) {
        return postJpaRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
    }
}
