package com.framgia.attendance.logic;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.unit.Seasar2;

import com.framgia.attendance.entity.DayData;
import com.framgia.attendance.util.DayType;

@RunWith(Seasar2.class)
public class CalcWorkTimeLogicTest {

    @Binding CalcWorkTimeLogic calcTimeLogic;

    @Test
    public void calc_workTime1() {
        assertThat(calcTimeLogic.calcTimeLag(1000, 1500), is(300));
    }

    @Test
    public void calc_workTime2() {
        assertThat(calcTimeLogic.calcTimeLag(1000, 1530), is(330));
    }

    @Test
    public void calc_workTime3() {
        assertThat(calcTimeLogic.calcTimeLag(1050, 1530), is(280));
    }

    @Test
    public void calc_overlap_time1() {
        assertThat(calcTimeLogic.calcOverlapTime(0, 1000, 500, 1500), is(300));
    }

    @Test
    public void calc_overlap_time2() {
        assertThat(calcTimeLogic.calcOverlapTime(500, 1500, 0, 1000), is(300));
    }

    @Test
    public void calc_overlap_time3() {
        assertThat(calcTimeLogic.calcOverlapTime(500, 1500, 600, 1000), is(240));
    }

    @Test
    public void calc_overlap_time4() {
        assertThat(calcTimeLogic.calcOverlapTime(600, 1000, 500, 1500), is(240));
    }

    @Test
    public void calc_overlap_time_not_overlaped1() {
        assertThat(calcTimeLogic.calcOverlapTime(600, 1000, 1100, 1500), is(0));
    }

    @Test
    public void calc_overlap_time_not_overlaped2() {
        assertThat(calcTimeLogic.calcOverlapTime(1100, 1500, 600, 1000), is(0));
    }

    @Test
    public void calc_late_night_time1() {
        DayData dayData = new DayData();
        dayData.setStartTime(300);
        dayData.setEndTime(3000);

        assertThat(calcTimeLogic.calcWorkTimeLateNight(dayData), is(540));
    }

    @Test
    public void calc_late_night_time2() {
        DayData dayData = new DayData();
        dayData.setStartTime(300);
        dayData.setEndTime(4800);

        assertThat(calcTimeLogic.calcWorkTimeLateNight(dayData), is(660));
    }

    @Test
    public void calc_late_night_time3() {
        DayData dayData = new DayData();
        dayData.setStartTime(300);
        dayData.setEndTime(400);

        assertThat(calcTimeLogic.calcWorkTimeLateNight(dayData), is(60));
    }

    @Test
    public void calc_late_night_time4() {
        DayData dayData = new DayData();
        dayData.setStartTime(900);
        dayData.setEndTime(2200);

        assertThat(calcTimeLogic.calcWorkTimeLateNight(dayData), is(0));
    }

    @Test
    public void calc_late_night_time5() {
        DayData dayData = new DayData();
        dayData.setStartTime(2200);
        dayData.setEndTime(2600);

        assertThat(calcTimeLogic.calcWorkTimeLateNight(dayData), is(240));
    }

    @Test
    public void calc_late_night_time6() {
        DayData dayData = new DayData();
        dayData.setStartTime(4700);
        dayData.setEndTime(4800);

        assertThat(calcTimeLogic.calcWorkTimeLateNight(dayData), is(60));
    }

    @Test
    public void calc_work_time_holiday_sunday1() {
        DayData dayData = new DayData();
        dayData.setDay(DayType.LEGAL_HOLIDAY);
        dayData.setStartTime(2200);
        dayData.setEndTime(3100);
        dayData.setBreakTimeTotal(60);

        assertThat(calcTimeLogic.calcWorkTimeHoliday(dayData), is(120));
    }

    @Test
    public void calc_work_time_holiday_sunday2() {
        DayData dayData = new DayData();
        dayData.setDay(DayType.LEGAL_HOLIDAY);
        dayData.setStartTime(2100);
        dayData.setEndTime(3100);
        dayData.setBreakTimeTotal(60);

        assertThat(calcTimeLogic.calcWorkTimeHoliday(dayData), is(120));
    }

    @Test
    public void calc_work_time_holiday_weekday() {
        DayData dayData = new DayData();
        dayData.setYearMonthNum(201408);
        dayData.setDayNum(21);
        dayData.setDay(DayType.WEEKDAY);
        dayData.setStartTime(1800);
        dayData.setEndTime(2600);
        dayData.setBreakTimeTotal(60);

        assertThat(calcTimeLogic.calcWorkTimeHoliday(dayData), is(0));
    }

    @Test
    public void calc_work_time_holiday_saturday() {
        DayData dayData = new DayData();
        dayData.setYearMonthNum(201408);
        dayData.setDayNum(2);
        dayData.setDay(DayType.REGULAR_HOLIDAY);
        dayData.setStartTime(2200);
        dayData.setEndTime(3100);
        dayData.setBreakTimeTotal(60);

        assertThat(calcTimeLogic.calcWorkTimeHoliday(dayData), is(360));
    }
}
