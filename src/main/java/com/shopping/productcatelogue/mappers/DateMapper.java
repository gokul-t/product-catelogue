package com.shopping.productcatelogue.mappers;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class DateMapper {

    public OffsetDateTime asOffsetDateTime(Timestamp timestamp) {
        return Optional.ofNullable(timestamp)
                .map(ts -> OffsetDateTime.of(ts.toLocalDateTime().getYear(), ts.toLocalDateTime().getMonthValue(),
                        ts.toLocalDateTime().getDayOfMonth(), ts.toLocalDateTime().getHour(),
                        ts.toLocalDateTime().getMinute(),
                        ts.toLocalDateTime().getSecond(), ts.toLocalDateTime().getNano(), ZoneOffset.UTC))
                .orElse(null);
    }

    public Timestamp asTimestamp(OffsetDateTime offsetDateTime) {
        return Optional.ofNullable(offsetDateTime)
                .map(o -> Timestamp.valueOf(o.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()))
                .orElse(null);
    }

}
