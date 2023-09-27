package com.project.socket.recruit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "recruit_id")
  private Long id;                  // 모집분야 ID

  @Column(name = "field_name")
  private String fieldName;         // 분야 이름


  @Builder
  public Recruit(Long id, String fieldName) {
    this.id = id;
    this.fieldName = fieldName;
  }
}
