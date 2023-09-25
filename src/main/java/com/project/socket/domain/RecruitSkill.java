package com.project.socket.domain;

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

@Table(name = "recruitskill")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitSkill {

    @Id   @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_skill_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")        //단방향 매핑
    private Recruit rsRecruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill rsSkill;                  //단방향 매핑

  @Builder
  public RecruitSkill(Long id, Recruit rsRecruit, Skill rsSkill) {
    this.id = id;
    this.rsRecruit = rsRecruit;
    this.rsSkill = rsSkill;
  }
}
