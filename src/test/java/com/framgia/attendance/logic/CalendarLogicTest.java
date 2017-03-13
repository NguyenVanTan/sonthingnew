package com.framgia.attendance.logic;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.framgia.attendance.entity.Holiday;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.unit.Seasar2;

import com.framgia.attendance.web.employee.YearMonth;

@RunWith(Seasar2.class)
public class CalendarLogicTest {

    @Binding CalendarLogic calendarLogic;

    @Test
    public void calc_business_day_num1() {
        List<Holiday> holidays = new ArrayList<>();

        Holiday holiday = new Holiday();
        holiday.setYearMonthNum(201401);
        holiday.setDayNum(1);
        holiday.setHolidayTyp(1);
        holidays.add(holiday);

        holiday = new Holiday();
        holiday.setYearMonthNum(201401);
        holiday.setDayNum(2);
        holiday.setHolidayTyp(2);
        holidays.add(holiday);

        assertThat(calendarLogic.calcBusinessDayNum(new YearMonth(201401), holidays), is(21));
    }

    @Test
    public void calc_business_day_num2() {
        List<Holiday> holidays = new ArrayList<>();

        Holiday holiday = new Holiday();
        holiday.setYearMonthNum(201405);
        holiday.setDayNum(1);
        holiday.setHolidayTyp(1);
        holidays.add(holiday);

        holiday = new Holiday();
        holiday.setYearMonthNum(201405);
        holiday.setDayNum(2);
        holiday.setHolidayTyp(2);
        holidays.add(holiday);

        assertThat(calendarLogic.calcBusinessDayNum(new YearMonth(201405), holidays), is(20));
    }

    @Test
    public void check_sunday() {
        assertThat(calendarLogic.isBusinessDay(new YearMonth(201401), 19), is(false));
    }

    @Test
    public void check_monday() {
        assertThat(calendarLogic.isBusinessDay(new YearMonth(201401), 20), is(true));
    }

    @Test
    public void check_saturday() {
        assertThat(calendarLogic.isBusinessDay(new YearMonth(201401), 25), is(false));
    }

    @Test
    public void check_holiday() {
        assertThat(calendarLogic.isBusinessDay(new YearMonth(201401), 2), is(false));
    }

    @Test
    public void check_holiday_and_sunday() {
        assertThat(calendarLogic.isBusinessDay(new YearMonth(201405), 4), is(false));
    }

    @Test
    public void check_saturday2() {
        Calendar instance = Calendar.getInstance();
        instance.set(2014, 0, 11);
        assertThat(calendarLogic.isSundayOrSaturday(instance), is(true));
    }

}
