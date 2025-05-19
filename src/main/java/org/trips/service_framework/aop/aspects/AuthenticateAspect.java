package org.trips.service_framework.aop.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.trips.service_framework.aop.Authenticate;
import org.trips.service_framework.audit.dtos.AllowedPermissions;
import org.trips.service_framework.clients.response.RealmSessionInfoResponse;
import org.trips.service_framework.exceptions.AccessDeniedException;
import org.trips.service_framework.exceptions.UnauthorizedException;
import org.trips.service_framework.services.AuthService;
import org.trips.service_framework.utils.Context;
import org.trips.service_framework.utils.CookieUtils;
import org.trips.service_framework.utils.HttpUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private final AllowedPermissions allowedPermissions;

    @Around("@annotation(authenticate)")
    public Object validateAuthHeader(ProceedingJoinPoint joinPoint, org.trips.service_framework.aop.Authenticate authenticate) throws Throwable {
        HttpServletRequest request = HttpUtils.getRequest();

        String namespaceId = HttpUtils.readMandatoryHeader(request, NAMESPACE_ID_HEADER);
        Context.setNamespaceId(namespaceId);

        String userId;
        if (Boolean.TRUE.equals(isAuthEnabled)) {
            String clientId = HttpUtils.readHeader(request, CLIENT_ID_HEADER);
            String clientSecret = HttpUtils.readHeader(request, CLIENT_SECRET_HEADER);

            Cookie accessToken = CookieUtils.readCookie(request, ACCESS_TOKEN_COOKIE);
            Cookie refreshTokenId = CookieUtils.readCookie(request, REFRESH_TOKEN_ID_COOKIE);

            if (Objects.nonNull(accessToken) && Objects.nonNull(refreshTokenId)) {
                boolean authorizationRequired = authenticate.authorize();
                RealmSessionInfoResponse.UserDetail userDetail = authService.authenticateCookieSession(List.of(accessToken, refreshTokenId), authorizationRequired);
                if (authorizationRequired) {
                    Set<String> userPermissions = userDetail.getPermissions();
                    Set<String> allowedPermissions = getAllowedPermissions(request);
                    if (userPermissions.stream().noneMatch(allowedPermissions::contains))
                        throw new AccessDeniedException("User doesn't have the required permissions");
                }
                userId = userDetail.getUserId();
            } else if (Objects.nonNull(clientId) && Objects.nonNull(clientSecret))
                userId = authService.authenticateClientIdSecret(clientId, clientSecret);
            else
                throw new UnauthorizedException("Authentication failed!! Auth credentials missing in the header");
        } else {
            userId = SYSTEM;
        }

        Context.setUserId(userId);

        Object response = joinPoint.proceed();

        Context.clean();
        return response;
    }

    private Set<String> getAllowedPermissions(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String key = String.format("%s:%s", method, requestURI);
        return allowedPermissions.getPermissions(key);
    }
}
