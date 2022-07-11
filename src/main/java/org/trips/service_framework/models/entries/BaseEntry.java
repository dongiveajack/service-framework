package org.trips.service_framework.models.entries;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created By Abhinav Tripathi
 */
@Data
public abstract class BaseEntry implements Serializable {
    private Long id;
    private String createdBy;
    private String updatedBy;
    private Date createdAt;
    private Date updatedAt;
    protected Long version;
}
