package com.boda.onlineqandaspring.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        log.debug("拦截器执行: URI={}, ContextPath={}", requestURI, contextPath);

        // 白名单已在 WebConfig 中配置，这里只需检查 session
        HttpSession session = request.getSession(false); // 不创建新session

        if (session == null) {
            log.debug("Session 不存在，重定向到登录页面");
            response.sendRedirect(contextPath + "/user/login");
            return false;
        }

        Object userID = session.getAttribute("userID");

        if (userID == null) {
            log.debug("用户未登录，重定向到登录页面");
            response.sendRedirect(contextPath + "/user/login");
            return false;
        }

        log.debug("用户已登录 (userID={})，放行请求", userID);
        return true;
    }
}