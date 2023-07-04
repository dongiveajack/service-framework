package org.trips.service_framework.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.trips.service_framework.clients.RealmClient;
import org.trips.service_framework.clients.response.RealmClientsVerifyResponse;
import org.trips.service_framework.clients.response.RealmSessionInfoResponse;
import org.trips.service_framework.exceptions.AccessDeniedException;
import org.trips.service_framework.utils.StringUtils;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Objects;

/**
 * @author Anupam Dagar on 02/11/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService {
    private final RealmClient realmClient;

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

    /*//@Cacheable(cacheNames = CacheConfig.REALM_USER_CACHE, unless = "#result != null")
    public UserInfo getUser(String userId) {
        RealmUserSearchResponse response = realmClient.getUsers(RealmUserSearchBody.of(List.of(userId)));
        if (response.getStatus().getCode() != 200) {
            return null;
        }

        return Optional.ofNullable(response.getData())
                .map(RealmUserSearchResponse.Data::getWhitelistedUsers)
                .orElseThrow(() -> new NotFoundException("No whitelisted user data found in response"))
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        String.format("No valid whitelisted user data found in response for ID: %s", userId)));
    }

    public Map<String, UserInfo> getUsers(List<String> ids) {
//        Cache realmUserCache = Optional.ofNullable(cacheManager.getCache(CacheConfig.REALM_USER_CACHE)).orElseThrow(() -> CacheNotFoundException.ofName(CacheConfig.REALM_USER_CACHE));

        List<String> nonCachedUserIds = ids.stream()
                .filter(id -> Objects.isNull(realmUserCache.get(id)))
                .collect(Collectors.toList());

        RealmUserSearchResponse response = realmClient.getUsers(RealmUserSearchBody.of(nonCachedUserIds));

        Map<String, UserInfo> uncachedUserInfoList = Optional.ofNullable(response.getData())
                .map(RealmUserSearchResponse.Data::getWhitelistedUsers)
                .orElseThrow(() -> new NotFoundException("No whitelisted user data found in response"))
                .stream()
                .collect(Collectors.toMap(UserInfo::getId, x -> x));

        uncachedUserInfoList.forEach(realmUserCache::put);

        return ids.stream().collect(Collectors.toMap(x -> x, x -> Optional.ofNullable(realmUserCache.get(x, UserInfo.class)).orElse(new UserInfo())));
    }*/
}
