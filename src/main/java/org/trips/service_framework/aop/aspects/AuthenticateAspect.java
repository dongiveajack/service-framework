package org.trips.service_framework.aop.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.trips.service_framework.services.AuthService;
import org.trips.service_framework.utils.Context;
import org.trips.service_framework.utils.CookieUtils;
import org.trips.service_framework.utils.HttpUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

import static org.trips.service_framework.utils.Constants.*;

/**
 * @author Abhinav Tripathi 16/05/21
 */
@Slf4j
@Aspect
@Component
@Configuration
@RequiredArgsConstructor
public class AuthenticateAspect {

    @Value("${realm.authentication.enabled:true}")
    private Boolean isAuthEnabled;

    private final AuthService authService;

    @Around("@annotation(org.trips.service_framework.aop.Authenticate)")
    public Object validateAuthHeader(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = HttpUtils.getRequest();

        String namespaceId = HttpUtils.readMandatoryHeader(request, NAMESPACE_ID_HEADER);

        String userId;
        if (Boolean.TRUE.equals(isAuthEnabled)) {
            String clientId = HttpUtils.readHeader(request, CLIENT_ID_HEADER);
            String clientSecret = HttpUtils.readHeader(request, CLIENT_SECRET_HEADER);

            Cookie accessToken = CookieUtils.readCookie(request, ACCESS_TOKEN_COOKIE);
            Cookie refreshTokenId = CookieUtils.readCookie(request, REFRESH_TOKEN_ID_COOKIE);

            if (Objects.nonNull(accessToken) && Objects.nonNull(refreshTokenId))
                userId = authService.authenticateCookieSession(List.of(accessToken, refreshTokenId));
            else if (Objects.nonNull(clientId) && Objects.nonNull(clientSecret))
                userId = authService.authenticateClientIdSecret(clientId, clientSecret);
            else
                throw new AccessDeniedException("User Authentication failed!! Auth credentials missing in the header");
        } else {
            userId = SYSTEM;
        }

        Context.setNamespaceId(namespaceId);
        Context.setUserId(userId);

        Object response = joinPoint.proceed();

        Context.clean();
        return response;
    }
}
