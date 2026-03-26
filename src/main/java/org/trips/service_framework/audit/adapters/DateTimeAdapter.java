package org.trips.service_framework.audit.adapters;

import org.javers.core.json.BasicStringTypeAdapter;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.stereotype.Component;


/**
 * @author anomitra on 16/08/24
 */

@Component
public class DateTimeAdapter extends BasicStringTypeAdapter<DateTime> {

    @Override
    public String serialize(DateTime sourceValue) {
        return sourceValue.toString();
    }

    @Override
    public DateTime deserialize(String serializedValue) {
        return ISODateTimeFormat.dateTime().parseDateTime(serializedValue);
    }

    @Override
    public Class getValueType() {
        return DateTime.class;
    }
}

