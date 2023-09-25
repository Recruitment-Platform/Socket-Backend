package com.project.socket.domain;

import com.project.socket.common.model.BaseTime;
import com.project.socket.domain.enumtype.PostType;
import com.project.socket.domain.enumtype.Status;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;                                  //게시물 ID

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")    // 단방향 매핑
  private User user;

  private String title;                            // 게시글 제목

  @Column(name = "content")
  private String postContent;                      // 게시글 내용


  @Enumerated(EnumType.STRING)
  @Column(name = "post_status")
  private Status postStatus;                     // 게시글 상태(CREATED/MODIFIED/DELETED)


  @Enumerated(EnumType.STRING)
  @Column(name = "post_type")
  private PostType postType;                     // 게시글 타입(PROJECT/STUDY)


  @Builder
  public Post(Long id, User user, String title, String postContent, Status postStatus,
      PostType postType) {
    this.id = id;
    this.user = user;
    this.title = title;
    this.postContent = postContent;
    this.postStatus = postStatus;
    this.postType = postType;
  }
}
