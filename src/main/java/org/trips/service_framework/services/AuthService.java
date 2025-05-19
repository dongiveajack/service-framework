package org.trips.service_framework.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.trips.service_framework.clients.RealmClient;
import org.trips.service_framework.clients.request.RealmAuthenticateRequest;
import org.trips.service_framework.clients.request.RealmUserSearchRequest;
import org.trips.service_framework.clients.response.RealmClientsVerifyResponse;
import org.trips.service_framework.clients.response.RealmSessionInfoResponse;
import org.trips.service_framework.clients.response.RealmUser;
import org.trips.service_framework.clients.response.RealmUserResponse;
import org.trips.service_framework.configs.CacheConfig;
import org.trips.service_framework.exceptions.AccessDeniedException;
import org.trips.service_framework.exceptions.CacheNotFoundException;
import org.trips.service_framework.exceptions.NotFoundException;
import org.trips.service_framework.exceptions.UnauthorizedException;
import org.trips.service_framework.utils.StringUtils;

import javax.servlet.http.Cookie;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Anupam Dagar on 02/11/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService {
    private final RealmClient realmClient;
    private final CacheManager cacheManager;

    @Value("${service.client-id}")
    private String clientId;

    public RealmSessionInfoResponse.UserDetail authenticateCookieSession(List<Cookie> cookies, boolean authorizationRequired) {
        StringBuilder cookieBuilder = new StringBuilder();
        for (Cookie cookie : cookies) {
            cookieBuilder.append(StringUtils.concatWithSeparatorExtension("=", ";", cookie.getName(), cookie.getValue()));
        }

        List<String> clientIds = authorizationRequired ? List.of(clientId) : List.of();
        RealmSessionInfoResponse response = realmClient.authorize(cookieBuilder.toString(), RealmAuthenticateRequest.of(clientIds));
        if (response.getStatus().getCode() != 200) {
            throw new UnauthorizedException("Authentication Failed! Invalid Access Token");
        }
        return response.getData();
    }

    public String authenticateClientIdSecret(String clientId, String clientSecret) {
        RealmClientsVerifyResponse response = realmClient.verifyClientIdSecret(clientId, clientSecret);
        if (response.getStatus().getCode() != 200) {
            throw new UnauthorizedException("Authentication Failed! Invalid Access Token");
        }
        if (!Objects.nonNull(response.getData()) || !response.getData().isActive()) {
            throw new UnauthorizedException("Authentication Failed! Invalid Access Token");
        }
        return response.getData().getClientId();
    }

    public Map<String, RealmUser> getUsers(Collection<String> ids) {
        Cache realmUserCache = Optional.ofNullable(cacheManager.getCache(CacheConfig.REALM_USER_CACHE))
                .orElseThrow(() -> CacheNotFoundException.ofName(CacheConfig.REALM_USER_CACHE));

        Set<String> nonCachedUserIds = ids.stream()
                .filter(id -> Objects.isNull(realmUserCache.get(id)))
                .collect(Collectors.toSet());

        RealmUserResponse response = realmClient.getUsers(RealmUserSearchRequest.of(nonCachedUserIds));

        Map<String, RealmUser> uncachedUserInfoList = Optional.ofNullable(response.getData())
                .map(RealmUserResponse.Data::getWhitelistedUsers)
                .orElseThrow(() -> new NotFoundException("No whitelisted user data found in response"))
                .stream()
                .collect(Collectors.toMap(RealmUser::getId, x -> x));

        uncachedUserInfoList.forEach(realmUserCache::put);

        return ids.stream().collect(Collectors.toMap(x -> x, x -> Optional.ofNullable(realmUserCache.get(x, RealmUser.class)).orElse(new RealmUser())));
    }
}
