package org.example.utils.json;

import jakarta.json.bind.adapter.JsonbAdapter;
import java.time.OffsetDateTime;
import java.util.Date;

public class IsoDateAdapter implements JsonbAdapter<Date, String> {

    @Override
    public String adaptToJson(Date value) {
        return value.toInstant().toString();
    }

    @Override
    public Date adaptFromJson(String value) {
        return Date.from(OffsetDateTime.parse(value).toInstant());
    }
}
