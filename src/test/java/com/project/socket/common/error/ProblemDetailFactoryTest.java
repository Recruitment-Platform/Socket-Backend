package com.project.socket.common.error;

import static com.project.socket.common.error.ErrorCode.OAUTH2_LOGIN_FAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ProblemDetailFactoryTest {

  @Test
  void 에러코드에_해당하는_ProblemDetail을_생성한다() {
    ProblemDetail problemDetail = ProblemDetailFactory.from(OAUTH2_LOGIN_FAIL);

    assertAll(
        () -> assertThat(problemDetail.getStatus()).isEqualTo(OAUTH2_LOGIN_FAIL.getStatus()),
        () -> assertThat(problemDetail.getProperties().get("code"))
            .isEqualTo(OAUTH2_LOGIN_FAIL.getCode())
    );
  }

  @Test
  void 에러코드와_instanceURI에_해당하는_ProblemDetail을_생성한다() {
    URI instanceURI = URI.create("/some/request");
    ProblemDetail problemDetail = ProblemDetailFactory.of(OAUTH2_LOGIN_FAIL,
        instanceURI.toString());

    assertAll(
        () -> assertThat(problemDetail.getStatus()).isEqualTo(OAUTH2_LOGIN_FAIL.getStatus()),
        () -> assertThat(problemDetail.getDetail()).isEqualTo(OAUTH2_LOGIN_FAIL.getMessage()),
        () -> assertThat(problemDetail.getProperties().get("code"))
            .isEqualTo(OAUTH2_LOGIN_FAIL.getCode()),
        () -> assertThat(problemDetail.getInstance()).isEqualTo(instanceURI)
    );
  }
}