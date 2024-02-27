package com.everyones.lawmaking.global.service;

import com.everyones.lawmaking.domain.entity.AuthInfo;
import com.everyones.lawmaking.domain.entity.Provider;
import com.everyones.lawmaking.domain.entity.User;
import com.everyones.lawmaking.global.auth.PrincipalDetails;
import com.everyones.lawmaking.global.auth.socialInfo.OAuth2UserInfo;
import com.everyones.lawmaking.global.auth.socialInfo.OAuth2UserInfoFactory;
import com.everyones.lawmaking.repository.AuthInfoRepository;
import com.everyones.lawmaking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthInfoRepository authInfoRepository;
    private final UserRepository userRepository;



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            log.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new InternalAuthenticationServiceException(ex.getMessage());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {

        var provider = Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        log.info(String.valueOf(user.getAttributes()));
        log.info(String.valueOf(user));

        // 소셜로그인에서 사용자 정보를 가져왔음
        var userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, user.getAttributes());

        log.info(String.valueOf(provider));

        var userSocialId = userInfo.getId();

        // 저장된 AuthInfo 찾아서 값이 있으면 우리가 찾는 사용자인지 확인
        var savedAuthInfo = authInfoRepository.findBySocialIdAndProvider(userSocialId, provider)
                .orElseGet(() -> createAuthAndUser(userInfo,provider));

        return PrincipalDetails.create(savedAuthInfo, user.getAttributes());


    }

    private AuthInfo createAuthAndUser(OAuth2UserInfo userInfo, Provider provider) {
        // AuthInfo에 데이터 저장
        AuthInfo authInfo = AuthInfo.builder()
                .socialId(userInfo.getId())
                .provider(provider)
                .build();
        // User에 데이터 저장
        User user = User.builder()
                .authInfo(authInfo)
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .build();
        userRepository.saveAndFlush(user);

        return authInfoRepository.saveAndFlush(authInfo);
    }




}