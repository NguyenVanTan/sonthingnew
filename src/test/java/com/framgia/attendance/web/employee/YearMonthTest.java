package com.framgia.attendance.web.employee;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class YearMonthTest {

    @Test
    public void testDateConstructor() {
        final Date preHistorycalDate = new Date(0L);
        final YearMonth yearmonath = new YearMonth(preHistorycalDate);

        assertThat(yearmonath.getYear(), is(1970));
        assertThat(yearmonath.getMonth(), is(1));
    }

    @Test
    public void testEquals() {
        final YearMonth one = new YearMonth(2002, 5);
        final YearMonth other = new YearMonth(2002, 5);
        final YearMonth another = new YearMonth(2002, 4);
        final YearMonth slightly = new YearMonth(2003, 5);
        final YearMonth completely = new YearMonth(2001, 6);

        assertThat(one, is(one));
        assertThat(one, is(other));
        assertThat(one, is(not(another)));
        assertThat(one, is(not(slightly)));
        assertThat(one, is(not(completely)));
    }

    @Test
    public void testBeforeAfter() {
        final YearMonth one = new YearMonth(2002, 5);
        final YearMonth other = new YearMonth(2002, 5);
        final YearMonth another1 = new YearMonth(2002, 4);
        final YearMonth another2 = new YearMonth(2002, 6);
        final YearMonth slightly1 = new YearMonth(2001, 5);
        final YearMonth slightly2 = new YearMonth(2003, 5);

        assertThat(one.before(one), is(false));
        assertThat(one.after(one), is(false));
        assertThat(one.before(other), is(false));
        assertThat(one.after(other), is(false));
        assertThat(one.before(another1), is(false));
        assertThat(one.after(another1), is(true));
        assertThat(one.before(another2), is(true));
        assertThat(one.after(another2), is(false));
        assertThat(one.before(slightly1), is(false));
        assertThat(one.after(slightly1), is(true));
        assertThat(one.before(slightly2), is(true));
        assertThat(one.after(slightly2), is(false));
    }

    @Test
    public void testGetDate() {
        final Date now = new Date();
        final YearMonth nowYearMonth = new YearMonth(now);

        final Calendar calendar1 = Calendar.getInstance();
        final Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(now);
        calendar2.setTime(nowYearMonth.getDate());

        assertThat(calendar2.get(Calendar.YEAR), is(calendar1.get(Calendar.YEAR)));
        assertThat(calendar2.get(Calendar.MONTH), is(calendar1.get(Calendar.MONTH)));
        assertThat(calendar2.get(Calendar.DATE), is(1));
    }
}
