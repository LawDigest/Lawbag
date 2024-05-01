package com.everyones.lawmaking.global.util;

import com.everyones.lawmaking.domain.entity.Provider;
import com.everyones.lawmaking.domain.entity.User;
import com.everyones.lawmaking.global.error.CustomException;
import com.everyones.lawmaking.global.ResponseCode;
import com.everyones.lawmaking.global.auth.PrincipalDetails;
import com.everyones.lawmaking.global.config.AppProperties;
import com.everyones.lawmaking.global.error.ErrorCode;
import com.everyones.lawmaking.global.jwt.AuthToken;
import com.everyones.lawmaking.global.jwt.AuthTokenProvider;
import com.everyones.lawmaking.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    private TokenUtil(){
        throw new UnsupportedOperationException("TokenUtil class");
    }

    @Builder
    public static class IssueTokenArgsDto{

        @NotNull
        private UserRepository userRepository;

        @NotNull
        private Authentication authentication;

        @NotNull
        private AppProperties.Auth auth;

        @NotNull
        private AuthTokenProvider tokenProvider;
    }

    @Builder
    public static class ReissueTokenArgsDto{

        @NotNull
        private UserRepository userRepository;

        @NotNull
        private AppProperties.Auth auth;

        @NotNull
        private AuthTokenProvider tokenProvider;

        @NotNull
        private String accessToken;

        @NotNull
        private String refreshTokenFromCookie;
    }


    public static Map<String, AuthToken> issueToken(IssueTokenArgsDto issueTokenArgsDto) {


        var authentication = issueTokenArgsDto.authentication;
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        var userLocalId = principalDetails.getUserId();
        var userSocialId = principalDetails.getSocialId();
        var userRole = principalDetails.getRole();
        var userProvider = principalDetails.getProviderType();




        Map<String, AuthToken> authTokenMap = new HashMap<>();
        // access 토큰 설정
        var now = new Date();

        long minutes = 1000 * 60;

        AuthToken accessToken = issueTokenArgsDto.tokenProvider.createAuthToken(
                userSocialId,
                userRole.getCode(),
                userLocalId,
                new Date(now.getTime() + issueTokenArgsDto.auth.getAccessTokenExpiry()*minutes)
        );

        // refresh 토큰 설정

        AuthToken refreshToken = issueTokenArgsDto.tokenProvider.createAuthToken(
                userSocialId,
                userProvider.name(),
                new Date(now.getTime() + issueTokenArgsDto.auth.getRefreshTokenExpiry()*minutes)
        );

        var savedUser = issueTokenArgsDto.userRepository.findUserBySocialIdAndProvider(userSocialId, userProvider)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));

        savedUser.setRefreshToken(refreshToken.getToken());
        issueTokenArgsDto.userRepository.save(savedUser);

        authTokenMap.put("accessToken", accessToken);
        authTokenMap.put("refreshToken", refreshToken);


        return authTokenMap;
    }

    public static Map<String, AuthToken> reissueToken(ReissueTokenArgsDto reissueTokenArgsDto) {

        AuthToken refreshToken = reissueTokenArgsDto.tokenProvider.convertAuthToken(reissueTokenArgsDto.refreshTokenFromCookie);

        try {
            if (!refreshToken.validate()) {
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        // 엑세스토큰과 리프레시토큰의 socialId 비교
        AuthToken token = reissueTokenArgsDto.tokenProvider.convertAuthToken(reissueTokenArgsDto.accessToken);


        String socialIdFromAT = token.getTokenClaims().getSubject();
        String socialIdFromRT = refreshToken.getTokenClaims().getSubject();
        String providerFromRT = ((String) refreshToken.getTokenClaims().get("provider"));

        if (!socialIdFromAT.equals(socialIdFromRT)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        User savedUser = reissueTokenArgsDto.userRepository.findBySocialIdAndProvider(socialIdFromRT, Enum.valueOf(Provider.class, providerFromRT))
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));

        String rTFromUser = savedUser.getRefreshToken();

        if (!rTFromUser.equals(reissueTokenArgsDto.refreshTokenFromCookie)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Map<String, AuthToken> authTokenMap = new HashMap<>();
        // access 토큰 설정
        var now = new Date();
        var role = savedUser.getRole();

        long minutes = 1000 * 60;

        AuthToken newAccessToken = reissueTokenArgsDto.tokenProvider.createAuthToken(
                savedUser.getAuthInfo().getSocialId(),
                role.getCode(),
                savedUser.getId(),
                new Date(now.getTime() + reissueTokenArgsDto.auth.getAccessTokenExpiry()*minutes)
        );

        // refresh 토큰 설정

        AuthToken newRefreshToken = reissueTokenArgsDto.tokenProvider.createAuthToken(
                savedUser.getAuthInfo().getSocialId(),
                savedUser.getAuthInfo().getProvider().name(),
                new Date(now.getTime() + reissueTokenArgsDto.auth.getRefreshTokenExpiry()*minutes)
        );

        savedUser.setRefreshToken(refreshToken.getToken());
        reissueTokenArgsDto.userRepository.save(savedUser);

        authTokenMap.put("accessToken", newAccessToken);
        authTokenMap.put("refreshToken", newRefreshToken);


        return authTokenMap;
    }

    private static boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }


}
