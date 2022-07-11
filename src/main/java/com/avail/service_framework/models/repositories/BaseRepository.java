package com.avail.service_framework.models.repositories;

import com.avail.service_framework.models.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Created By Abhinav Tripathi
 */
public interface BaseRepository<M extends BaseEntity> extends JpaRepository<M, Long>, JpaSpecificationExecutor<M> {
}
