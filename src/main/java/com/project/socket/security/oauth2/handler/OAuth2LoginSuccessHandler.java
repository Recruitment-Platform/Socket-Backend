package com.project.socket.security.oauth2.handler;

import static com.project.socket.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.project.socket.common.error.ErrorCode;
import com.project.socket.security.CookieUtils;
import com.project.socket.security.JwtProvider;
import com.project.socket.security.exception.RedirectBadRequestException;
import com.project.socket.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.project.socket.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.function.UnaryOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final UnaryOperator<User> userRepositoryOAuth2UserHandler;
  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
  private final JwtProvider jwtProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

    User savedUser = userRepositoryOAuth2UserHandler.apply(
        UserFactory.of(
            oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
            oAuth2AuthenticationToken.getPrincipal().getAttributes()));

    String targetUrl = determineTargetUrl(request, savedUser);

    clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  protected String determineTargetUrl(HttpServletRequest request, User user) {
    Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                                              .map(Cookie::getValue);

    String targetUri = redirectUri.orElseThrow(() ->
        new RedirectBadRequestException(ErrorCode.REDIRECT_BAD_REQUEST));

    return UriComponentsBuilder.fromUriString(targetUri)
                               .queryParam("accessToken", jwtProvider.createAccessToken(user))
                               .queryParam("refreshToken", jwtProvider.createRefreshToken(user))
                               .queryParam("profileSetup", user.isProfileSetup())
                               .build().toUriString();
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request,
      HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request,
        response);
  }
}
