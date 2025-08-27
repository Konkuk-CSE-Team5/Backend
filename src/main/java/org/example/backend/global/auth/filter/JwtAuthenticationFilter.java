package org.example.backend.global.auth.filter;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.global.auth.constant.JwtErrorCode;
import org.example.backend.global.auth.exception.InvalidJwtException;
import org.example.backend.global.auth.exception.JwtExpiredException;
import org.example.backend.global.auth.exception.JwtNotFoundException;
import org.example.backend.global.auth.security.CustomUserDetailsService;
import org.example.backend.global.auth.util.JwtClaimKey;
import org.example.backend.global.auth.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.example.backend.global.auth.constant.JwtAutenticationFilterConstants.*;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if(token != null){
            try{
                Claims claims = jwtUtil.validateJwt(token);

                // UserDetails 생성
                Long userId = claims.get(JwtClaimKey.USER_ID.getKey(), Long.class);

                // userDetails 조회
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(String.valueOf(userId));

                // 인증토큰을 생성하고 시큐리티 컨텍스트홀더에 저장
                Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // token을 argument resolver에 전달
                request.setAttribute(TOKEN_FOR_ARGUMENT_RESOLVER.getValue(), token);

            }
            // entryPoint에 전달할 에러 코드들
            catch(InvalidJwtException e){
                log.info("Invalid JWT Token", e);
                request.setAttribute(JWT_ERROR_CODE.getValue(), JwtErrorCode.INVALID_JWT_ERROR);
            }
            catch(JwtExpiredException e){
                log.info("Expired JWT Token", e);
                request.setAttribute(JWT_ERROR_CODE.getValue(), JwtErrorCode.EXPIRED_JWT_ERROR);
            }
            catch(JwtNotFoundException e){
                log.info("JWT claims string is empty.", e);
                request.setAttribute(JWT_ERROR_CODE.getValue(), JwtErrorCode.JWT_NOT_FOUND_ERROR);
            }
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request){
        String authorization = request.getHeader(AUTHORIZATION.getValue());
        if(authorization != null && authorization.startsWith(TOKEN_PREFIX.getValue())){
            String token = authorization.split(" ")[1];
            return token;
        }
        log.info("token does not exist!");
        return null;
    }
}
