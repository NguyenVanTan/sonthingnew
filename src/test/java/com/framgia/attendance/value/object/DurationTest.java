package com.framgia.attendance.value.object;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;

public class DurationTest {

    @Test
    public void testEquals() {
        final LocalDateTime from = LocalDateTime.of(2012, 2, 10, 0, 0);
        final LocalDateTime to = LocalDateTime.of(2012, 2, 14, 0, 0);;

        final Duration<LocalDateTime> that01 = new Duration<>(from, to);
        final Duration<LocalDateTime> that02 = new Duration<>(from, to);

        assertThat(that01, is(that02));
    }
}
