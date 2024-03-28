package com.everyones.lawmaking.global.config;

import com.everyones.lawmaking.global.filterException.RestAuthenticationEntryPoint;
import com.everyones.lawmaking.global.handler.OAuth2AuthenticationFailureHandler;
import com.everyones.lawmaking.global.handler.OAuth2AuthenticationSuccessHandler;
import com.everyones.lawmaking.global.handler.TokenAccessDeniedHandler;
import com.everyones.lawmaking.global.handler.TokenAuthenticationFilter;
import com.everyones.lawmaking.global.jwt.AuthTokenProvider;
import com.everyones.lawmaking.global.service.CustomOAuth2UserService;
import com.everyones.lawmaking.repository.AuthInfoRepository;
import com.everyones.lawmaking.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.everyones.lawmaking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Configuration 어노테이션 추가
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer { // WebMvcConfigurer 인터페이스 구현(implements)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/v1/images/**") // 핸들러 추가
                .addResourceLocations("classpath:/static/") // 클래스패스 설정시 끝에 꼭 / 넣어주자.
                .setCachePeriod(20); // 초단위
    }

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final CorsConfig corsConfig;
    private final UserRepository userRepository;
    private final AuthInfoRepository authInfoRepository;


    // 회원가입이랑 로그인 필요한 요청에 대해서만 시큐리티 필터를 타기
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors((cors) -> cors
                        .configurationSource(corsConfig.corsConfigurationSource()))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers("/v1/**/like**").hasRole("MEMBER")
                        .requestMatchers("/v1/**/bookmark**").hasRole("MEMBER")
                        .requestMatchers("/v1/**/follow**").hasRole("MEMBER")
                        .requestMatchers("/v1/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(tokenAccessDeniedHandler))

                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/v1/oauth2/authorization")
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                        )
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/*/oauth2/code/*")
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler())
                        .failureHandler(oAuth2AuthenticationFailureHandler()
                        )
                )
        ;

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }



    /*
     * 토큰 필터 설정
     * */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }



    /*
     * 쿠키 기반 인가 Repository
     * 인가 응답을 연계 하고 검증할 때 사용.
     * */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /*
     * Oauth 인증 성공 핸들러
     * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                userRepository,
                authInfoRepository,
                tokenProvider,
                appProperties,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    /*
     * Cors 설정
     * */

}

