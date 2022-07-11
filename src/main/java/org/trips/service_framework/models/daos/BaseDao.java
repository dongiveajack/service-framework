package org.trips.service_framework.models.daos;

import org.trips.service_framework.models.entities.BaseEntity;
import lombok.Data;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created By Abhinav Tripathi
 */
@Data
@Repository
public abstract class BaseDao<T extends BaseEntity> {
    @PersistenceContext
    private EntityManager em;

    //TODO implement generic search method
}
