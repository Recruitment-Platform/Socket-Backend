package com.project.socket.post.controller.dto.response;

import com.project.socket.common.model.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//Post에서 값을 가져올 데이터
public class PostResponseDto {

  private String title;
  private String content;
  private BaseTime created_At;
  private BaseTime modified_At;

  public PostResponseDto(String title, String content, BaseTime created_At,
      BaseTime modified_At) {
    this.title = title;
    this.content = content;
    this.created_At = created_At;
    this.modified_At = modified_At;
  }
}
