package com.project.socket.common.error;

import java.net.URI;
import org.springframework.http.ProblemDetail;

public class ProblemDetailFactory {

  private ProblemDetailFactory() {
  }

  public static ProblemDetail from(ErrorCode errorCode) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatus(errorCode.getStatus());
    problemDetail.setTitle(errorCode.getMessage());
    problemDetail.setProperty("code", errorCode.getCode());
    return problemDetail;
  }

  public static ProblemDetail of(ErrorCode errorCode, String instanceURI) {
    ProblemDetail problemDetail = from(errorCode);
    problemDetail.setInstance(URI.create(instanceURI));
    return problemDetail;
  }
}
