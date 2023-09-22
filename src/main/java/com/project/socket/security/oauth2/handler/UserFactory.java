package com.project.socket.security.oauth2.handler;

import com.project.socket.user.model.Role;
import com.project.socket.user.model.SocialProvider;
import com.project.socket.user.model.User;
import java.util.Map;
import java.util.Objects;

public class UserFactory {

  private UserFactory() {
  }

  public static User of(String provider, Map<String, Object> attribute) {
    return switch (provider) {
      case "google" -> googleUser(attribute);
      case "kakao" -> kakaoUser(attribute);
      case "github" -> githubUser(attribute);
      default -> throw new IllegalArgumentException();
    };
  }

  private static User googleUser(Map<String, Object> attribute) {
    return User.builder()
               .email(getValue(attribute, "email"))
               .role(Role.ROLE_USER)
               .socialProvider(SocialProvider.GOOGLE)
               .socialId(getValue(attribute, "sub"))
               .build();
  }

  private static User kakaoUser(Map<String, Object> attribute) {
    Map<String, Object> accountAttribute = getAttribute(attribute, "kakao_account");
    return User.builder()
               .email(getValue(accountAttribute, "email"))
               .role(Role.ROLE_USER)
               .socialProvider(SocialProvider.KAKAO)
               .socialId(getValue(attribute, "id"))
               .build();
  }

  private static User githubUser(Map<String, Object> attribute) {
    return User.builder()
               .socialProvider(SocialProvider.GITHUB)
               .role(Role.ROLE_USER)
               .socialId(getValue(attribute, "id"))
               .email(getValue(attribute, "email"))
               .build();
  }

  private static String getValue(Map<String, Object> attribute, String key) {
    Object value = attribute.get(key);
    if (Objects.isNull(value)) {
      return null;
    }
    return value.toString();
  }

  private static Map<String, Object> getAttribute(Map<String, Object> attribute, String key) {
    return (Map<String, Object>) attribute.get(key);
  }
}
