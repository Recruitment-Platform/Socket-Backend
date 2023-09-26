package com.project.socket.security.filter;

import com.project.socket.security.JwtProvider;
import com.project.socket.security.exception.InvalidJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String AUTHORIZATION_TYPE = "Bearer ";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String jwtToken = getJwtFromRequest(request);

    try {
      jwtProvider.validateToken(jwtToken);
      UserDetails userDetails = getUserDetails(jwtProvider.getSubjectFromToken(jwtToken),
          jwtProvider.getRoleFromToken(jwtToken));
      Authentication authentication = getAuthentication(userDetails);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (InvalidJwtException invalidJwtException) {
      request.setAttribute("exception", invalidJwtException.getErrorCode());
    }

    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_TYPE)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private Authentication getAuthentication(UserDetails userDetails) {
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  private UserDetails getUserDetails(String userId, String role) {
    return User.builder().username(userId).password("").authorities(role).build();

  }
}
