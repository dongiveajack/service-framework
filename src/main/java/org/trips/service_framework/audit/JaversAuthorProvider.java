package org.trips.service_framework.audit;

/*
  @author anomitra on 26/08/24
 */

import org.javers.spring.auditable.AuthorProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.trips.service_framework.utils.Context;

@Configuration
public class JaversAuthorProvider {

    @Bean
    public AuthorProvider provideJaversAuthor() {
        return new SimpleAuthorProvider();
    }

    private static class SimpleAuthorProvider implements AuthorProvider {
        @Override
        public String provide() {
            return Context.getUserId();
        }
    }
}
