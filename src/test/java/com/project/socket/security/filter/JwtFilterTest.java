package com.project.socket.security.filter;

import static com.project.socket.common.error.ErrorCode.INVALID_JWT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.socket.security.JwtProvider;
import com.project.socket.security.exception.InvalidJwtException;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

  @InjectMocks
  JwtFilter jwtFilter;

  @Mock
  JwtProvider jwtProvider;

  MockHttpServletRequest request = new MockHttpServletRequest();
  MockHttpServletResponse response = new MockHttpServletResponse();
  MockFilterChain filterChain = new MockFilterChain();


  final String AUTHORIZATION_HEADER = "Authorization";
  final String AUTHORIZATION_TYPE = "Bearer ";
  final String TOKEN = "Token";

  @AfterEach
  void resetHeader() {
    request.removeHeader(AUTHORIZATION_HEADER);
    SecurityContextHolder.clearContext();
  }

  @Test
  void 인증_헤더에_유효한_토큰이_주어지면_인증_객체가_저장된다() throws ServletException, IOException {
    request.addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_TYPE + TOKEN);

    doNothing().when(jwtProvider).validateToken(TOKEN);
    when(jwtProvider.getSubjectFromToken(TOKEN)).thenReturn("1L");
    when(jwtProvider.getRoleFromToken(TOKEN)).thenReturn("ROLE_USER");

    jwtFilter.doFilterInternal(request, response, filterChain);

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
  }

  @Test
  void 인증_헤더에_유효하지_않은_토큰이_주어지면_인증_객체가_저장되지_않는다() throws ServletException, IOException {
    request.addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_TYPE + TOKEN);

    doThrow(new InvalidJwtException("error", INVALID_JWT)).when(jwtProvider).validateToken(TOKEN);

    jwtFilter.doFilterInternal(request, response, filterChain);

    assertAll(
        () -> assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull(),
        () -> verify(jwtProvider, never()).getSubjectFromToken(anyString()),
        () -> verify(jwtProvider, never()).getRoleFromToken(anyString()),
        () -> assertThat(request.getAttribute("exception")).isNotNull()
    );
  }

  @Test
  void 인증_헤더가_없으면_인증_객체가_저장되지_않는다() throws ServletException, IOException {
    doThrow(new InvalidJwtException("error", INVALID_JWT)).when(jwtProvider).validateToken(null);

    jwtFilter.doFilterInternal(request, response, filterChain);

    assertAll(
        () -> assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull(),
        () -> verify(jwtProvider, never()).getSubjectFromToken(anyString()),
        () -> verify(jwtProvider, never()).getRoleFromToken(anyString()),
        () -> assertThat(request.getAttribute("exception")).isNotNull()
    );
  }

  @Test
  void 인증_타입이_다르면_인증_객체가_저장되지_않는다() throws ServletException, IOException {
    request.addHeader(AUTHORIZATION_HEADER, TOKEN);
    doThrow(new InvalidJwtException("error", INVALID_JWT)).when(jwtProvider).validateToken(null);

    jwtFilter.doFilterInternal(request, response, filterChain);

    assertAll(
        () -> assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull(),
        () -> verify(jwtProvider, never()).getSubjectFromToken(anyString()),
        () -> verify(jwtProvider, never()).getRoleFromToken(anyString()),
        () -> assertThat(request.getAttribute("exception")).isNotNull()
    );
  }
}