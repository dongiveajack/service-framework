package org.trips.service_framework.audit.dtos;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "")
public class AllowedPermissions {
    private Map<String, Set<String>> permissions;

    public Set<String> getPermissions(String apiKey) {
        return permissions.getOrDefault(apiKey, Set.of());
    }
}