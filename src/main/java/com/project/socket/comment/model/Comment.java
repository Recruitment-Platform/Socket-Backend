package com.project.socket.comment.model;

import com.project.socket.common.model.BaseTime;
import com.project.socket.post.Post;
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
  private User user;

  private String content;                       // 댓글 내용

  @Enumerated(EnumType.STRING)
  @Column(name = "comment_status")
  private CommentStatus commentStatus;                       // 댓글 상태

  @Column(name = "group_num")
  private int groupNum;                        // 댓글 그룹

  @Column(name = "group_order")
  private int groupOrder;                      // 댓글과 대댓글 순서

  @Column(name = "comment_class")
  private int commentClass;                    // 계층

  @Builder
  public Comment(Long id, Post cPost, User user, String content, CommentStatus commentStatus,
      int groupNum,
      int groupOrder, int commentClass) {
    this.id = id;
    this.cPost = cPost;
    this.user = user;
    this.content = content;
    this.commentStatus = commentStatus;
    this.groupNum = groupNum;
    this.groupOrder = groupOrder;
    this.commentClass = commentClass;
  }
}
