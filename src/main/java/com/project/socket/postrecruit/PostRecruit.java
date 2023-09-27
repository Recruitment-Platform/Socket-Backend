package com.project.socket.postrecruit;

import com.project.socket.post.Post;
import com.project.socket.recruit.Recruit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "post_recruit")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRecruit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_recruit_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")           //단방향 매핑
  private Post prPost;                    // PostRecruit 클래스와 매핑하는 Post 타입 -> prpost

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recruit_id")        //단방향 매핑
  private Recruit prRecruit;

  @Builder
  public PostRecruit(Long id, Post prPost, Recruit prRecruit) {
    this.id = id;
    this.prPost = prPost;
    this.prRecruit = prRecruit;
  }
}
