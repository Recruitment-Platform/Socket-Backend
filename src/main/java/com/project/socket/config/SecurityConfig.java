package com.project.socket.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.project.socket.security.filter.JwtFilter;
import com.project.socket.security.handler.CustomAccessDeniedHandler;
import com.project.socket.security.handler.CustomAuthenticationEntryPoint;
import com.project.socket.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.project.socket.security.oauth2.handler.OAuth2LoginFailureHandler;
import com.project.socket.security.oauth2.handler.OAuth2LoginSuccessHandler;
import com.project.socket.user.model.Role;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
  private final JwtFilter jwtFilter;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .sessionManagement(sessionManager -> sessionManager
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2Login(oauth2Login -> oauth2Login
            .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
            .successHandler(oAuth2LoginSuccessHandler)
            .failureHandler(oAuth2LoginFailureHandler))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(withDefaults())
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/docs/**", "/actuator/**", "/error/**", "/").permitAll()
            .requestMatchers("/ws/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/posts/{postId}/comments").permitAll()
            .requestMatchers(HttpMethod.GET, "/posts/{postId}").permitAll()
            .requestMatchers(HttpMethod.GET, "/posts/projects").permitAll()
            .requestMatchers("/signup").authenticated()
            .requestMatchers(HttpMethod.POST, "/posts/{postId}/chat-rooms")
            .hasAuthority(Role.ROLE_USER.name())
            .anyRequest().authenticated())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler));
    return http.build();
  }

  @Value("${cors.allow-origins}")
  private List<String> corsOrigins;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOriginPatterns(corsOrigins);
    corsConfiguration.setAllowedHeaders(List.of("*"));
    corsConfiguration.setMaxAge(3600L);
    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}
