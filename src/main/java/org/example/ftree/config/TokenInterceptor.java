package org.example.ftree.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.ftree.service.EncValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private NoneAuthUri noneAuthUri;

    @Value("${project.login.token-salt}")
    private String loginTokenSalt;

    @Autowired
    private EncValueService encValueService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        //白名单
        if (noneAuthUri.isNoneAuthUri(requestURI)) {
            return true;
        }
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(authorization) && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring("Bearer ".length());
                String userId = encValueService.decrypt(token, loginTokenSalt);
                //存入 userId
                request.setAttribute("userId", userId);
                return true;
            } catch (Exception e) {
                log.info("解密用户Token失败：{}", authorization, e);
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }


}
