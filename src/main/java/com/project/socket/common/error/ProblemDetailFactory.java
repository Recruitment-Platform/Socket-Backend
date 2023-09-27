package com.project.socket.common.error;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ProblemDetailFactory {

  private ProblemDetailFactory() {
  }

  public static ProblemDetail from(ErrorCode errorCode) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatus(errorCode.getStatus());
    problemDetail.setTitle(HttpStatus.valueOf(errorCode.getStatus()).getReasonPhrase());
    problemDetail.setProperty("code", errorCode.getCode());
    return problemDetail;
  }

  public static ProblemDetail of(ErrorCode errorCode, String instanceURI) {
    ProblemDetail problemDetail = from(errorCode);
    problemDetail.setDetail(errorCode.getMessage());
    problemDetail.setInstance(URI.create(instanceURI));
    return problemDetail;
  }

  public static ProblemDetail of(ErrorCode errorCode, BindingResult bindingResult) {
    List<CustomFieldError> fieldErrors = CustomFieldError.of(bindingResult);
    ProblemDetail problemDetail = from(errorCode);
    problemDetail.setProperty("detail", fieldErrors);
    return problemDetail;
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CustomFieldError {
    private String field;
    private String value;
    private String reason;

    private CustomFieldError(final String field, final String value, final String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }

    private static List<CustomFieldError> of(final BindingResult bindingResult) {
      final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
      return fieldErrors.stream()
                        .map(error -> new CustomFieldError(
                            error.getField(),
                            String.valueOf(error.getRejectedValue()),
                            error.getDefaultMessage()))
                        .collect(Collectors.toList());
    }
  }
}
