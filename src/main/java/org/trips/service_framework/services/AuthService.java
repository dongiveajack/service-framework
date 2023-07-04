package org.trips.service_framework.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.trips.service_framework.clients.RealmClient;
import org.trips.service_framework.clients.request.RealmUserSearchRequest;
import org.trips.service_framework.clients.response.RealmClientsVerifyResponse;
import org.trips.service_framework.clients.response.RealmSessionInfoResponse;
import org.trips.service_framework.clients.response.RealmUser;
import org.trips.service_framework.clients.response.RealmUserResponse;
import org.trips.service_framework.configs.CacheConfig;
import org.trips.service_framework.exceptions.AccessDeniedException;
import org.trips.service_framework.exceptions.CacheNotFoundException;
import org.trips.service_framework.exceptions.NotFoundException;
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

    public String authenticateCookieSession(List<Cookie> cookies) {
        StringBuilder cookieBuilder = new StringBuilder();
        for (Cookie cookie : cookies) {
            cookieBuilder.append(StringUtils.concatWithSeparatorExtension("=", ";", cookie.getName(), cookie.getValue()));
        }

        RealmSessionInfoResponse response = realmClient.getSessionInfo(cookieBuilder.toString());
        if (response.getStatus().getCode() != 200) {
            throw new AccessDeniedException("forbidden: invalid access token");
        }
        return response.getData().getUserId();
    }

    public String authenticateClientIdSecret(String clientId, String clientSecret) {
        RealmClientsVerifyResponse response = realmClient.verifyClientIdSecret(clientId, clientSecret);
        if (response.getStatus().getCode() != 200) {
            throw new AccessDeniedException("forbidden: invalid access token");
        }
        if (!Objects.nonNull(response.getData()) || !response.getData().isActive()) {
            throw new AccessDeniedException("forbidden: invalid clientId or secret");
        }
        return response.getData().getClientId();
    }

    public Map<String, RealmUser> getUsers(List<String> ids) {
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
