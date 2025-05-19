package org.trips.service_framework.audit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.trips.service_framework.audit.dtos.AuditEntry;
import org.trips.service_framework.audit.dtos.ChangeDetail;
import org.trips.service_framework.clients.response.RealmUser;
import org.trips.service_framework.services.AuthService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author anomitra on 20/12/24
 */

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final Javers javers;
    private final ObjectMapper mapper;
    private final AuthService authService;

    @SneakyThrows
    public Pair<List<AuditEntry>, Map<String, RealmUser>> getAuditLogs(Long id, String entityClass) {
        Changes changes = javers.findChanges(QueryBuilder.byInstanceId(id, Class.forName(entityClass)).build());

        Set<String> userIds = new HashSet<>();

        List<AuditEntry> groupedChanges = changes.groupByCommit()
                .stream()
                .map(x -> {
                            CommitMetadata commitMetadata = x.getCommit();
                            List<Change> commitChanges = x.get();

                            userIds.add(commitMetadata.getAuthor());

                            List<ChangeDetail> changesList = commitChanges
                                    .stream()
                                    .map(this::fetchChangeDetails)
                                    .collect(Collectors.toList());

                            return AuditEntry.builder()
                                    .commitId(commitMetadata.getId().toString())
                                    .commitAuthor(commitMetadata.getAuthor())
                                    .commitDate(commitMetadata.getCommitDate())
                                    .entityClass(entityClass)
                                    .entityId(id)
                                    .changes(changesList)
                                    .build();
                        }
                ).collect(Collectors.toList());

        Map<String, RealmUser> userInfoMap = authService.getUsers(userIds);

        return Pair.of(
                groupedChanges,
                userInfoMap
        );
    }

    private ChangeDetail fetchChangeDetails(Change change) {
        if (change instanceof PropertyChange<?>) {
            PropertyChange<?> pc = (PropertyChange<?>) change;
            String propertyName = pc.getPropertyName();
            if (StringUtils.isNotBlank(propertyName)) {
                return ChangeDetail.builder()
                        .property(propertyName)
                        .changeType(change.getClass().getSimpleName())
                        .left(Objects.nonNull(pc.getLeft()) ? pc.getLeft() : "")
                        .right(Objects.nonNull(pc.getRight()) ? pc.getRight() : "")
                        .build();
            }
        }
        return null;
    }
}
