package com.project.socket.common.controller;

import com.project.socket.comment.model.CommentStatus;
import com.project.socket.common.model.EnumType;
import com.project.socket.docs.EnumDocs;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class CommonDocController {

  @PostMapping("/error")
  public void sampleError(@RequestBody @Valid SampleRequest sampleRequest) {

  }

  @GetMapping("/enums")
  public ApiResponseDto<EnumDocs> findEnums() {

    // 문서화 하고 싶은 -> EnumDocs 클래스에 담긴 모든 Enum 값 생성
    Map<String, String> commentStatus = getDocs(CommentStatus.values());

    Map<String, String> postMeeting = getDocs(PostMeeting.values());
    Map<String, String> postType = getDocs(PostType.values());
    Map<String, String> postStatus = getDocs(PostStatus.values());

    // 전부 담아서 반환 -> 테스트에서는 이걸 꺼내 해석하여 조각을 만들면 된다.
    return ApiResponseDto.of(EnumDocs.builder()
        .commentStatus(commentStatus)
        .postMeeting(postMeeting)
        .postType(postType)
        .postStatus(postStatus)
        .build());
  }

  private Map<String, String> getDocs(EnumType[] enumTypes) {
    return Arrays.stream(enumTypes)
        .collect(Collectors.toMap(EnumType::getName, EnumType::getDescription));
  }

  public record SampleRequest(@NotBlank String name, @Email String email) {

  }
}
