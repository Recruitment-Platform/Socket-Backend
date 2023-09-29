package com.project.socket.postskill;

import com.project.socket.post.Post;
import com.project.socket.skill.Skill;
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

@Table(name = "post_skill")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSkill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_skill_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")          //단방향 매핑
  private Post psPost;                    // PostSkill 클래스와 매핑하는 Post 타입 ->pspost

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "skill_id")
  private Skill psSkill;    //단방향 매핑


  @Builder
  public PostSkill(Long id, Post psPost, Skill psSkill) {
    this.id = id;
    this.psPost = psPost;
    this.psSkill = psSkill;
  }
}
