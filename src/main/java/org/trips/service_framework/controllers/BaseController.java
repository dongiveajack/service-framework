package org.trips.service_framework.controllers;

import org.trips.service_framework.aop.Authenticate;
import org.trips.service_framework.services.BaseService;
import org.trips.service_framework.codes.SuccessCodes;
import org.trips.service_framework.models.entities.BaseEntity;
import org.trips.service_framework.models.entries.SearchEntry;
import org.trips.service_framework.models.responses.BaseResponse;
import org.trips.service_framework.models.responses.StatusResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created By Abhinav Tripathi
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class BaseController<R extends BaseResponse, M extends BaseEntity> {
    protected final BaseService<M> service;

    protected abstract R createResponse(List<M> entryList);

    @Authenticate
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R findById(@PathVariable Long id) throws RuntimeException {
        M result = service.find(id);
        R response = createResponse(Collections.singletonList(result));
        response.setStatus(new StatusResponse(SuccessCodes.DATA_RETRIEVED_SUCCESSFULLY, Objects.nonNull(result) ? 1 : 0));
        log.info("Data retrieved successfully {}", response);
        return response;
    }

    @Authenticate
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody M input) throws Exception {
        M result = getService().save(input);
        R response = createResponse(Collections.singletonList(result));
        response.setStatus(new StatusResponse(SuccessCodes.CREATED, 1));
        return response;
    }

    @Authenticate
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public R update(@RequestBody M input, @PathVariable Long id) throws Exception {
        M result = getService().update(input, id);
        R response = createResponse(Collections.singletonList(result));
        response.setStatus(new StatusResponse(SuccessCodes.OK, 1));
        return response;
    }

    @Authenticate
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public R delete(@PathVariable Long id) throws Exception {
        M result = getService().delete(id);
        R response = createResponse(Collections.singletonList(result));
        response.setStatus(new StatusResponse(SuccessCodes.OK, 1));
        return response;
    }

    @Authenticate
    @RequestMapping(value = "/bulk", method = RequestMethod.PUT)
    public R bulkUpdate(@RequestBody List<M> entries) throws Exception {
        List<M> updatedEntries = getService().bulkCreateOrUpdate(entries);
        R response = createResponse(updatedEntries);
        response.setStatus(new StatusResponse(SuccessCodes.OK, updatedEntries.size()));
        return response;
    }

    @Authenticate
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public R search(@RequestParam(value = "filters", required = false) String filters,
                    @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                    @RequestParam(value = "fetchSize", defaultValue = "1000", required = false) Integer fetchSize,
                    @RequestParam(value = "sortBy", required = false) String sortBy,
                    @RequestParam(value = "includes", required = false) String includes) throws Exception {
        Page<M> searchResults = service.search(filters, page, fetchSize, sortBy, includes);
        R response = createResponse(searchResults.getContent());
        response.setStatus(new StatusResponse(SuccessCodes.DATA_RETRIEVED_SUCCESSFULLY, Long.valueOf(searchResults.getTotalElements()).intValue()));
        return response;
    }

    @Authenticate
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public R customSearch(@RequestBody SearchEntry searchEntry) throws Exception {
        Page<M> searchResults = service.search(searchEntry);
        R response = createResponse(searchResults.getContent());
        response.setStatus(new StatusResponse(SuccessCodes.DATA_RETRIEVED_SUCCESSFULLY, Long.valueOf(searchResults.getTotalElements()).intValue()));
        return response;
    }
}
