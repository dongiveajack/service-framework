package org.trips.service_framework.utils;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Anupam Dagar on 19/10/22
 */
@Slf4j
@Component
public class StringUtils {
    @SneakyThrows
    public static List<String> getRealmIdsFromData(List<Object> data, Collection<Method> fieldGetters) {
        List<String> ids = Lists.newArrayList();
        for (Object datum : data) {
            List<String> collect = Lists.newArrayList();
            for (Method getter : fieldGetters) {
                Optional<String> id = Optional.ofNullable((String) getter.invoke(datum));
                id.ifPresent(collect::add);
            }
            ids.addAll(collect);
        }
        return ids.stream().distinct().collect(Collectors.toList());
    }

    public static String concatWithSeparatorExtension(String separator, String extension, String... strs) {
        return strs.length > 0 ? String.join(separator, strs).concat(extension) : org.apache.commons.lang3.StringUtils.EMPTY;
    }
}
