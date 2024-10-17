package com.epsi.workshop.fig.config;

import com.epsi.workshop.fig.service.BlockedService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class DomainBlockInterceptor implements HandlerInterceptor {

    private final BlockedService blockedService;

    public DomainBlockInterceptor(BlockedService blockedService) {
        this.blockedService = blockedService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestDomain = getDomainFromRequest(request);

        if (blockedService.isBlocked(requestDomain)) {
            response.sendRedirect("/blocked-page?domain=" + requestDomain + "&timeRemaining=" + blockedService.getTimeRemaining(requestDomain));
            return false;
        }

        return true;
    }

    private String getDomainFromRequest(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return url.replace("https://", "").replace("http://", "").split("/")[0];
    }
}

