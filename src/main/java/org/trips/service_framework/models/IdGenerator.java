package org.trips.service_framework.models;

import org.trips.service_framework.models.entities.BaseEntity;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created By Abhinav Tripathi on 20/12/19
 */
public class IdGenerator extends IdentityGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        BaseEntity entity = (BaseEntity) object;
        if (Objects.nonNull(entity.getId()) && entity.getId() > 0) {
            return entity.getId();
        }
        return super.generate(session, object);
    }
}
