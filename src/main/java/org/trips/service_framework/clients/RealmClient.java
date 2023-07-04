package org.trips.service_framework.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trips.service_framework.clients.request.RealmUserSearchBody;
import org.trips.service_framework.clients.response.RealmClientsVerifyResponse;
import org.trips.service_framework.clients.response.RealmSessionInfoResponse;
import org.trips.service_framework.clients.response.RealmUserResponse;

/**
 * @author hardikphalet
 * @since 20/02/23, Mon
 */

@Component
@FeignClient(name = "realm", url = "${realm.base-url}")
public interface RealmClient {
    @RequestMapping(method = RequestMethod.GET, value = "/sessioninfo")
    RealmSessionInfoResponse getSessionInfo(@RequestHeader("Cookie") String cookie);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/clients/verify")
    RealmClientsVerifyResponse verifyClientIdSecret(@RequestHeader("Client-Id") String clientId, @RequestHeader("Client-Secret") String clientSecret);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/whitelisted-users/search")
    RealmUserResponse getUsers(@RequestBody RealmUserSearchBody searchBody);
}
