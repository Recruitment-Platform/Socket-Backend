package com.project.socket.common.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApiResponseDto<T> {
  private T data;

  private ApiResponseDto(T data){
    this.data=data;
  }

  public static <T> ApiResponseDto<T> of(T data) {
    return new ApiResponseDto<>(data);
  }
}
