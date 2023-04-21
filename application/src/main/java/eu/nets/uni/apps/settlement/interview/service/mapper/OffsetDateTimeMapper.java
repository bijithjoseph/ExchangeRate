package eu.nets.uni.apps.settlement.interview.service.mapper;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

@Component
public class OffsetDateTimeMapper {
    private final Clock clock;

    public OffsetDateTimeMapper(Clock clock) {
        this.clock = Objects.requireNonNull(clock);
    }

    public OffsetDateTime map(LocalDateTime time) {
        return Objects.isNull(time) ? null : time.atZone(clock.getZone()).toOffsetDateTime();
    }

    public LocalDateTime map(OffsetDateTime time) {
        return Objects.isNull(time) ? null : time.toLocalDateTime();
    }
}
