package com.ck.todoapp.util;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

public class TestClock extends Clock {

    private final Clock baseClock;

    private Clock clock;

    public TestClock(final Clock baseClock) {
        this.baseClock = baseClock;
        this.clock = baseClock;
    }

    public void addTimeDuration(final Duration duration) {
        clock = Clock.offset(clock, duration);
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(final ZoneId zone) {
        return clock.withZone(zone);
    }

    @Override
    public Instant instant() {
        return clock.instant();
    }

    public void reset() {
        clock = baseClock;
    }
}