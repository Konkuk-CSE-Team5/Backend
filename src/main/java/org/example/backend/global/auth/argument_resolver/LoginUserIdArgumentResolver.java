package org.example.backend.global.auth.argument_resolver;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.global.auth.annotation.LoginUserId;
import org.example.backend.global.auth.util.JwtClaimKey;
import org.example.backend.global.auth.util.JwtUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.example.backend.global.auth.constant.JwtAutenticationFilterConstants.TOKEN_FOR_ARGUMENT_RESOLVER;


@Slf4j
@RequiredArgsConstructor
public class LoginUserIdArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtUtil jwtUtil;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class) && parameter.hasParameterAnnotation(LoginUserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = (String) request.getAttribute(TOKEN_FOR_ARGUMENT_RESOLVER.getValue());
        if(token == null) {
            return 1L;
        }
        Long userId = jwtUtil.validateJwt(token).get(JwtClaimKey.USER_ID.getKey(), Long.class);
        log.info("userId = {}",  userId);
        return userId;
    }
}
