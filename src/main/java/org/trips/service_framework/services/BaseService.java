package org.trips.service_framework.services;

import org.joda.time.DateTime;
import org.trips.service_framework.models.CustomSearchSpecification;
import org.trips.service_framework.models.entities.BaseEntity;
import org.trips.service_framework.models.entries.SearchEntry;
import org.trips.service_framework.models.repositories.BaseRepository;
import org.trips.service_framework.utils.Context;
import org.trips.service_framework.utils.SearchHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created By Abhinav Tripathi
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class BaseService<Entity extends BaseEntity> {
    public final BaseRepository<Entity> repository;
    public final Class<Entity> entityClass;

    protected abstract Entity merge(Entity entity, Entity newEntity);

    @Transactional(readOnly = true)
    public Entity find(Long id) throws RuntimeException {
        Optional<Entity> entity = getRepository().findById(id);
        return entity.orElse(null);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Entity save(Entity entity) throws RuntimeException {
        return getRepository().save(entity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Entity update(Entity entry, Long id) throws RuntimeException {
        Entity entity = getRepository().getReferenceById(id);
        return getRepository().save(merge(entry, entity));
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public List<Entity> bulkCreateOrUpdate(List<Entity> entries) throws RuntimeException {
        List<Long> entityIds = entries.stream()
                .filter(Objects::nonNull)
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
        List<Entity> entities = getRepository().findAllById(entityIds);
        Map<Long, Entity> entityMap = entities.stream().collect(Collectors.toMap(BaseEntity::getId, entity -> entity));
        List<Entity> updatedEntities = new ArrayList<>();
        for (Entity entry : entries) {
            Entity currentEntity = entityMap.get(entry.getId());
            if (Objects.nonNull(currentEntity)) {
                updatedEntities.add(merge(entry, currentEntity));
            } else {
                updatedEntities.add(entry);
            }
        }
        updatedEntities = getRepository().saveAll(updatedEntities);
        return updatedEntities;
    }

    @Transactional(readOnly = true)
    public Page<Entity> search(String filters, Integer page, Integer fetchSize, String sortBy, String includes) throws RuntimeException {
        Pageable pageable = SearchHelper.getPageRequest(page, fetchSize, sortBy);
        Page<Entity> resultPages = getRepository().findAll(new CustomSearchSpecification<>(filters), pageable);
        if (Objects.nonNull(includes))
            getResultWithIncludes(entityClass, includes, resultPages.getContent());
        return resultPages;
    }

    @Transactional(readOnly = true)
    public Page<Entity> search(SearchEntry searchEntry) throws RuntimeException {
        Pageable pageable = SearchHelper.getPageRequest(searchEntry.getPage(), searchEntry.getFetchSize(), searchEntry.getSortBy());
        Page<Entity> resultPages = getRepository().findAll(new CustomSearchSpecification<>(searchEntry.getFilters()), pageable);
        if (Objects.nonNull(searchEntry.getIncludes()))
            getResultWithIncludes(entityClass, searchEntry.getIncludes(), resultPages.getContent());
        return resultPages;
    }

    private void getResultWithIncludes(Class<Entity> clazz, String includes, List<Entity> result) throws RuntimeException {
        String[] includeArray = includes.split(",");
        Method[] classMethods = clazz.getDeclaredMethods();
        for (Entity data : result) {
            for (String includeKey : includeArray) {
                String methodName = String.format("get%s", StringUtils.capitalize(includeKey));
                for (Method classMethod : classMethods) {
                    if (classMethod.getName().equals(methodName)) {
                        try {
                            Hibernate.initialize(classMethod.invoke(data));
                        } catch (Exception e) {
                            log.error("Error Occurred while fetching lazy loaded entity");
                        }
                    }
                }
            }
        }
    }
    @Transactional(rollbackFor = RuntimeException.class)
    public Entity delete(Long id) {
        Entity entity = getRepository().getReferenceById(id);
        entity.setDeletedAt(DateTime.now());
        entity.setDeletedBy(Objects.nonNull(Context.getUserId()) ? Context.getUserId() : "System");
        return getRepository().save(entity);
    }
}
