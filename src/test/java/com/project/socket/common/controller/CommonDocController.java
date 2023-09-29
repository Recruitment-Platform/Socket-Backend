package com.project.socket.common.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonDocController {

  @PostMapping("/sample/error")
  public void sampleError(@RequestBody @Valid SampleRequest sampleRequest) {

  }

  public record SampleRequest(@NotBlank String name, @Email String email) {}
}
