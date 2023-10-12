package com.project.socket.user.model;

import com.project.socket.common.model.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@Getter
@NoArgsConstructor
public class User extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column
  private String nickname;

  @Column
  private String githubLink;

  @Column
  private String email;

  @Column(nullable = false)
  private boolean profileSetup;

  @Column(nullable = false)
  private String socialId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SocialProvider socialProvider;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Builder
  public User(Long userId, String nickname, String githubLink, String email, boolean profileSetup,
      String socialId, SocialProvider socialProvider, Role role) {
    this.userId = userId;
    this.nickname = nickname;
    this.githubLink = githubLink;
    this.email = email;
    this.profileSetup = profileSetup;
    this.socialId = socialId;
    this.socialProvider = socialProvider;
    this.role = role;
  }

  public void userNickname(String nickname) {
    this.nickname = nickname;
  }

  public void updateAdditionalInfo(String nickname, String githubLink) {
    this.nickname = nickname;
    this.githubLink = githubLink;
  }
}
