package com.project.socket.comment.model;

import com.project.socket.common.model.BaseTime;
import com.project.socket.post.model.Post;
import com.project.socket.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;                               //댓글 ID

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")              // 단방향매핑
  private Post cPost;                       // Comment 클래스와 매핑하는 Post 타입 ->cpost

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")   //단방향매핑
  private User writer;

  private String content;                       // 댓글 내용

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parentComment;

  @Enumerated(EnumType.STRING)
  @Column(name = "comment_status")
  private CommentStatus commentStatus;                       // 댓글 상태

  public static Comment createNewComment(Post postToAddComment, User writer, String content) {
    return Comment.builder()
                  .writer(writer).cPost(postToAddComment)
                  .content(content)
                  .commentStatus(CommentStatus.CREATED)
                  .build();
  }

  public void setupParentComment(Comment parentComment) {
    this.parentComment = parentComment;
  }

  public boolean validateRelation(Long writerId, Long postId) {
    return this.writer.getUserId().equals(writerId) && this.cPost.getId().equals(postId);

  }

  public void modifyContent(String content) {
    this.content = content;
    this.commentStatus = CommentStatus.MODIFIED;
  }

  @Builder
  public Comment(Long id, Post cPost, User writer, String content, Comment parentComment,
      CommentStatus commentStatus) {
    this.id = id;
    this.cPost = cPost;
    this.writer = writer;
    this.content = content;
    this.parentComment = parentComment;
    this.commentStatus = commentStatus;
  }
}
