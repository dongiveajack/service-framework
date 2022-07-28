package org.trips.service_framework.models.entities;

import org.trips.service_framework.utils.Context;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created By Abhinav Tripathi
 */
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "idGenerator", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "idGenerator", strategy = "org.trips.service_framework.models.IdGenerator")
    @Column(name = "id", updatable = false)
    protected Long id;

    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @Column(name = "updated_by", nullable = false)
    protected String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    protected DateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    protected DateTime updatedAt;

    @Version
    @Column(name = "version")
    protected Long version = 0L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    protected DateTime deletedAt;

    @Column(name = "deleted_by")
    protected String deletedBy;

    @PrePersist
    protected void onCreate() {
        updatedAt = createdAt = (Objects.isNull(createdAt) ? new DateTime() : createdAt);
        if (Objects.isNull(createdBy)) {
            if (Objects.nonNull(Context.getUserId())) {
                createdBy = Context.getUserId();
            } else {
                createdBy = "System";
            }
            updatedBy = createdBy;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new DateTime();
        this.updatedBy = Objects.nonNull(Context.getUserId()) ? Context.getUserId() : "System";
    }

}
