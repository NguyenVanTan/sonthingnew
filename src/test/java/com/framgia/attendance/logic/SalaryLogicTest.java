package com.framgia.attendance.logic;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import com.framgia.attendance.entity.Holiday;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Test;
import org.seasar.framework.container.annotation.tiger.Binding;

import com.framgia.attendance.dao.RankDao;
import com.framgia.attendance.entity.Employee;
import com.framgia.attendance.entity.MonthData;
import com.framgia.attendance.entity.Rank;
import com.framgia.attendance.exception.InvalidMonthDataException;
import com.framgia.attendance.test.AttendanceWicketTest;
import com.framgia.attendance.web.employee.YearMonth;

import java.util.ArrayList;
import java.util.List;

public class SalaryLogicTest extends AttendanceWicketTest {

    @Mocked @Binding public CalendarLogic calendarLogic;
    @Binding public SalaryLogic salaryLogic;
    @Mocked public RankDao rankDao;
    @Mocked public Rank rank;

    @Test(expected = InvalidMonthDataException.class)
    public void testCalcSalaryForMonthData_monthDataIsNull() throws InvalidMonthDataException {
        MonthData monthData = null;
        salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
    }

    @Test(expected = InvalidMonthDataException.class)
    public void testCalcSalaryForMonthData_workingHoursPlansIsNull() throws InvalidMonthDataException {
        MonthData monthData = new MonthData();
        monthData.setWorkingHoursPlan(null);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeTotal(1000);
        monthData.setWorkTimeLateNight(20);
        monthData.setWorkTimeHoliday(100);
        monthData.setTakenVacationDays(0.0);
        monthData.setBasePay(1000.0);
        monthData.setUnitPay(100.0);
        monthData.setPayVacationLeftJoinBeforefinalize(3.0);
        monthData.setOvertimePayDeemed(100.0);
        salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
    }

    @Test(expected = InvalidMonthDataException.class)
    public void testCalcSalaryForMonthData_workTimeTotalIsNull() throws InvalidMonthDataException {
        MonthData monthData = new MonthData();
        monthData.setWorkTimeTotal(null);
        monthData.setYearMonthNum(201407);
        monthData.setWorkingHoursPlan(800.0);
        monthData.setWorkingDaysPlan(100);
        monthData.setWorkTimeLateNight(20);
        monthData.setWorkTimeHoliday(100);
        monthData.setTakenVacationDays(0.0);
        monthData.setBasePay(1000.0);
        monthData.setUnitPay(100.0);
        monthData.setPayVacationLeftJoinBeforefinalize(3.0);
        monthData.setOvertimePayDeemed(100.0);
        salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
    }

    // Case 6
    @Test(expected = InvalidMonthDataException.class)
    public void testCalcSalaryForMonthData_workTimeHolidaytIsNull1() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setWorkTimeHoliday(null);
        monthData.setYearMonthNum(201407);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(120 * 60);
        monthData.setWorkingHoursPlan(80.0);
        monthData.setWorkingDaysPlan(80 / 8);
        monthData.setWorkTimeLateNight(20);
        monthData.setTakenVacationDays(0.0);
        monthData.setBasePay(1000.0);
        monthData.setUnitPay(100.0);
        monthData.setOvertimePayDeemed(100.0);
        monthData.setAbsenceHour(0.0);
        salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
    }

    // Case 7
    @Test
    public void testCalcSalaryForMonthData_workTimeHolidaytIsNull2() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(120 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setWorkTimeHoliday(null);
        monthData.setYearMonthNum(201407);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(80 * 60);
        monthData.setWorkingHoursPlan(120.0);
        monthData.setWorkingDaysPlan(120 / 8);
        monthData.setWorkTimeLateNight(20);
        monthData.setTakenVacationDays(0.0);
        monthData.setBasePay(1000.0);
        monthData.setUnitPay(100.0);
        monthData.setAbsenceHour(0.0);

        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(80 * 60));
        assertThat(result.getWorkTimeLateNight().intValue(), is(20));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(120.0));
        assertNull(result.getWorkTimeHoliday());
        assertThat(result.getAbsenceHour().intValue(), is(40));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(1000.0));
        assertThat(result.getOvertimePayEstimate().intValue(), is(0));
        assertThat(result.getOvertimePay().intValue(), is(0));
        assertThat(result.getMonthlySalary().intValue(), is(-3000));
    }

    // Case 8
    @Test
    public void testCalcSalaryForMonthData_workTimeHolidaytIsNull3() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(120 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setWorkTimeHoliday(null);
        monthData.setYearMonthNum(201407);
        monthData.setPayVacationLeftJoinBeforefinalize(2.0);
        monthData.setWorkTimeTotal(80 * 60);
        monthData.setWorkingHoursPlan(120.0);
        monthData.setWorkingDaysPlan(120 / 8);
        monthData.setWorkTimeLateNight(20);
        monthData.setTakenVacationDays(1.0);
        monthData.setBasePay(1000.0);
        monthData.setUnitPay(100.0);
        monthData.setAbsenceHour(0.0);

        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(80 * 60));
        assertThat(result.getWorkTimeLateNight().intValue(), is(20));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(120.0));
        assertNull(result.getWorkTimeHoliday());
        assertThat(result.getAbsenceHour().doubleValue(), is(40.0));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(1000.0));
        assertThat(result.getOvertimePayEstimate().intValue(), is(0));
        assertThat(result.getOvertimePay().intValue(), is(0));
        assertThat(result.getMonthlySalary().intValue(), is(-3000));
    }

    // Case 9
    @Test(expected = InvalidMonthDataException.class)
    public void testCalcSalaryForMonthData_workTimeLateNightIsNull1() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setWorkTimeLateNight(null);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeHoliday(20);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(120 * 60);
        monthData.setWorkingHoursPlan(80.0);
        monthData.setWorkingDaysPlan(80 / 8);
        monthData.setTakenVacationDays(0.0);
        monthData.setBasePay(1000.0);
        monthData.setUnitPay(100.0);
        monthData.setAbsenceHour(0.0);
        salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
    }

    // Case 10
    @Test
    public void testCalcSalaryForMonthData_workTimeLateNightIsNull2() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(120 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setWorkTimeLateNight(null);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeHoliday(20);
        monthData.setPayVacationLeftJoinBeforefinalize(1.0);
        monthData.setPayVacationLeftJoinTempBeforefinalize(2.0);
        monthData.setPayVacationLeftLastYearBeforefinalize(4.0);
        monthData.setPayVacationLeftThisYearBeforefinalize(5.0);
        // convert 83.2*60 = 832*6
        monthData.setWorkTimeTotal(832 * 6);
        monthData.setWorkingHoursPlan(120.0);
        monthData.setWorkingDaysPlan(120 / 8);
        monthData.setTakenVacationDays(10.0);
        monthData.setBasePay(1000.0);
        monthData.setUnitPay(100.0);
        monthData.setAbsenceHour(0.0);

        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(832 * 6));
        assertThat(result.getWorkTimeHoliday().intValue(), is(20));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(120.0));
        assertNull(result.getWorkTimeLateNight());
        assertThat(result.getAbsenceHour().doubleValue(), is(36.8));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(1000.0));
        assertThat(result.getOvertimePayEstimate().intValue(), is(0));
        assertThat(result.getOvertimePay().intValue(), is(0));
        assertThat(result.getMonthlySalary().doubleValue(), is(-2680.0));
    }

    // Case 11
    @Test
    public void testCalcSalaryForMonthData_workTimeLateNightIsNull3() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(120 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setWorkTimeLateNight(null);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeHoliday(20);
        monthData.setPayVacationLeftJoinBeforefinalize(1.0);
        monthData.setPayVacationLeftJoinTempBeforefinalize(4.0);
        monthData.setPayVacationLeftLastYearBeforefinalize(6.0);
        monthData.setPayVacationLeftThisYearBeforefinalize(5.0);
        // convert 73.6*60 = 736*6
        monthData.setWorkTimeTotal(736 * 6);
        monthData.setWorkingHoursPlan(120.0);
        monthData.setWorkingDaysPlan(120 / 8);
        monthData.setTakenVacationDays(10.0);
        monthData.setBasePay(1000.0);
        monthData.setUnitPay(100.0);
        monthData.setAbsenceHour(0.0);
        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(736 * 6));
        assertThat(result.getWorkTimeHoliday().intValue(), is(20));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(120.0));
        assertNull(result.getWorkTimeLateNight());
        assertThat(result.getAbsenceHour().doubleValue(), is(46.4));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(1000.0));
        assertThat(result.getOvertimePayEstimate().intValue(), is(0));
        assertThat(result.getOvertimePay().intValue(), is(0));
        assertThat(result.getMonthlySalary().intValue(), is(-3640));
    }



    // Case 13
    @Test
    public void testCalcSalaryForMonthData_calAllowanceLessThanWorkTimeHoliday1() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(80 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeLateNight(20 * 60);
        monthData.setWorkTimeHoliday(40 * 60);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(108 * 60);
        monthData.setWorkingHoursPlan(80.0);
        monthData.setWorkingDaysPlan(80 / 8);
        monthData.setTakenVacationDays(1.0);
        monthData.setBasePay(10000.0);
        monthData.setUnitPay(100.0);
        monthData.setOvertimePayDeemed(4000.0);
        monthData.setAbsenceHour(0.0);

        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(108 * 60));
        assertThat(result.getWorkTimeHoliday().intValue(), is(40 * 60));
        assertThat(result.getWorkTimeLateNight().intValue(), is(20 * 60));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(80.0));
        assertThat(result.getAbsenceHour().doubleValue(), is(0.0));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(10000.0));
        assertThat(result.getOvertimePayDeemed().doubleValue(), is(4000.0));
        assertThat(result.getOvertimePayEstimate().doubleValue(), is(4700.0));
        assertThat(result.getOvertimePay().doubleValue(), is(700.0));
        assertThat(result.getMonthlySalary().intValue(), is(10700));
    }

    // Case 14
    @Test
    public void testCalcSalaryForMonthData_calAllowanceLessThanWorkTimeHoliday2() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(80 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeLateNight(20 * 60);
        monthData.setWorkTimeHoliday(40 * 60);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(128 * 60);
        monthData.setWorkingHoursPlan(80.0);
        monthData.setWorkingDaysPlan(80 / 8);
        monthData.setTakenVacationDays(1.0);
        monthData.setBasePay(10000.0);
        monthData.setUnitPay(100.0);
        monthData.setOvertimePayDeemed(6800.0);
        monthData.setAbsenceHour(0.0);

        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(128 * 60));
        assertThat(result.getWorkTimeHoliday().intValue(), is(40 * 60));
        assertThat(result.getWorkTimeLateNight().intValue(), is(20 * 60));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(80.0));
        assertThat(result.getAbsenceHour().doubleValue(), is(0.0));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(10000.0));
        assertThat(result.getOvertimePayDeemed().doubleValue(), is(6800.0));
        assertThat(result.getOvertimePayEstimate().doubleValue(), is(6700.0));
        assertThat(result.getOvertimePay().doubleValue(), is(0.0));
        assertThat(result.getMonthlySalary().intValue(), is(10000));
    }

    // Case 15
    @Test
    public void testCalcSalaryForMonthData_calAllowanceLessThanWorkTimeHoliday3() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(80 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeLateNight(20 * 60);
        monthData.setWorkTimeHoliday(40 * 60);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(80 * 60);
        monthData.setWorkingHoursPlan(80.0);
        monthData.setWorkingDaysPlan(80 / 8);
        monthData.setTakenVacationDays(0.0);
        monthData.setBasePay(10000.0);
        monthData.setUnitPay(100.0);
        monthData.setOvertimePayDeemed(1000.0);
        monthData.setAbsenceHour(0.0);

        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(80 * 60));
        assertThat(result.getWorkTimeHoliday().intValue(), is(40 * 60));
        assertThat(result.getWorkTimeLateNight().intValue(), is(20 * 60));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(80.0));
        assertThat(result.getAbsenceHour().doubleValue(), is(0.0));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(10000.0));
        assertThat(result.getOvertimePayDeemed().doubleValue(), is(1000.0));
        assertThat(result.getOvertimePayEstimate().doubleValue(), is(1900.0));
        assertThat(result.getOvertimePay().doubleValue(), is(900.0));
        assertThat(result.getMonthlySalary().doubleValue(), is(10900.0));
    }

    // Case 16
    @Test
    public void testCalcSalaryForMonthData_calAllowanceLess60h1() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(80 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeLateNight(20 * 60);
        monthData.setWorkTimeHoliday(40 * 60);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(128 * 60);
        monthData.setWorkingHoursPlan(80.0);
        monthData.setWorkingDaysPlan(80 / 8);
        monthData.setTakenVacationDays(1.0);
        monthData.setBasePay(10000.0);
        monthData.setUnitPay(100.0);
        monthData.setOvertimePayDeemed(1000.0);
        monthData.setAbsenceHour(0.0);
        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(128 * 60));
        assertThat(result.getWorkTimeHoliday().intValue(), is(40 * 60));
        assertThat(result.getWorkTimeLateNight().intValue(), is(20 * 60));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(80.0));
        assertThat(result.getAbsenceHour().doubleValue(), is(0.0));
        assertThat(result.getPayVacationLeftAfterfinalize(), is(9.0));
        assertThat(result.getPayVacationLeftJoinBeforefinalize(), is(10.0));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(10000.0));
        assertThat(result.getOvertimePayDeemed().doubleValue(), is(1000.0));
        assertThat(result.getOvertimePayEstimate().doubleValue(), is(6700.0));
        assertThat(result.getOvertimePay().doubleValue(), is(5700.0));
        assertThat(result.getMonthlySalary().doubleValue(), is(15700.0));
    }

    // Case 17
    @Test
    public void testCalcSalaryForMonthData_calAllowanceLess60h2() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(80 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeLateNight(20 * 60);
        monthData.setWorkTimeHoliday(40 * 60);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(138 * 60);
        monthData.setWorkingHoursPlan(80.0);
        monthData.setWorkingDaysPlan(80 / 8);
        monthData.setTakenVacationDays(1.0);
        monthData.setBasePay(10000.0);
        monthData.setUnitPay(100.0);
        monthData.setOvertimePayDeemed(1000.0);
        monthData.setAbsenceHour(0.0);

        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(138 * 60));
        assertThat(result.getWorkTimeHoliday().intValue(), is(40 * 60));
        assertThat(result.getWorkTimeLateNight().intValue(), is(20 * 60));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(80.0));
        assertThat(result.getAbsenceHour().doubleValue(), is(0.0));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(10000.0));
        assertThat(result.getOvertimePayDeemed().doubleValue(), is(1000.0));
        assertThat(result.getOvertimePayEstimate().doubleValue(), is(7950.0));
        assertThat(result.getOvertimePay().doubleValue(), is(6950.0));
        assertThat(result.getMonthlySalary().doubleValue(), is(16950.0));
    }

    // Case 18
    @Test
    public void testCalcSalaryForMonthData_calAllowanceOver60h1() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(80 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeLateNight(20 * 60);
        monthData.setWorkTimeHoliday(40 * 60);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(188 * 60);
        monthData.setWorkingHoursPlan(80.0);
        monthData.setWorkingDaysPlan(80 / 8);
        monthData.setTakenVacationDays(1.0);
        monthData.setBasePay(10000.0);
        monthData.setUnitPay(100.0);
        monthData.setOvertimePayDeemed(1000.0);
        monthData.setAbsenceHour(0.0);

        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(188 * 60));
        assertThat(result.getWorkTimeHoliday().intValue(), is(40 * 60));
        assertThat(result.getWorkTimeLateNight().intValue(), is(20 * 60));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(80.0));
        assertThat(result.getAbsenceHour().doubleValue(), is(0.0));
        assertThat(result.getTakenVacationDays(), is(1.0));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(10000.0));
        assertThat(result.getOvertimePayDeemed().doubleValue(), is(1000.0));
        assertThat(result.getOvertimePayEstimate().doubleValue(), is(14200.0));
        assertThat(result.getOvertimePay().doubleValue(), is(13200.0));
        assertThat(result.getMonthlySalary().doubleValue(), is(23200.0));
    }

    // Case 19
    @Test
    public void testCalcSalaryForMonthData_calAllowanceOver60h2() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(80 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeLateNight(20 * 60);
        monthData.setWorkTimeHoliday(40 * 60);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(220 * 60);
        monthData.setWorkingHoursPlan(80.0);
        monthData.setWorkingDaysPlan(80 / 8);
        monthData.setTakenVacationDays(2.5);
        monthData.setBasePay(10000.0);
        monthData.setUnitPay(100.0);
        monthData.setOvertimePayDeemed(1000.0);
        monthData.setAbsenceHour(0.0);
        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(220 * 60));
        assertThat(result.getWorkTimeHoliday().intValue(), is(40 * 60));
        assertThat(result.getWorkTimeLateNight().intValue(), is(20 * 60));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(80.0));
        assertThat(result.getAbsenceHour().doubleValue(), is(0.0));
        assertThat(result.getPayVacationLeftAfterfinalize(), is(7.5));
        assertThat(result.getTakenVacationDays(), is(2.5));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(10000.0));
        assertThat(result.getOvertimePayDeemed().doubleValue(), is(1000.0));
        assertThat(result.getOvertimePayEstimate().doubleValue(), is(18400.0));
        assertThat(result.getOvertimePay().doubleValue(), is(17400.0));
        assertThat(result.getMonthlySalary().doubleValue(), is(27400.0));
    }


    // Case 21
    @Test
    public void testCalcSalaryForMonthData_calBasePay() throws InvalidMonthDataException {
        new NonStrictExpectations() {
            {
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
                calendarLogic.calcBusinessDayNum(new YearMonth(201407), (List<Holiday>) any);
                returns(160 / 8);
            }
        };
        Deencapsulation.setField(salaryLogic, "rankDao", rankDao);

        Employee employee = new Employee();

        MonthData monthData = new MonthData();
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201407);
        monthData.setWorkTimeLateNight(20 * 60);
        monthData.setWorkTimeHoliday(40 * 60);
        monthData.setPayVacationLeftJoinBeforefinalize(10.0);
        monthData.setWorkTimeTotal(220 * 60);
        monthData.setWorkingHoursPlan(80.0);
        monthData.setWorkingDaysPlan(80 / 8);
        monthData.setTakenVacationDays(2.5);
        monthData.setBasePay(10000.0);
        monthData.setUnitPay(100.0);
        monthData.setOvertimePayDeemed(1000.0);
        monthData.setAbsenceHour(0.0);
        MonthData result = salaryLogic.calcSalaryForMonthData(monthData, new ArrayList<>());
        assertThat(result.getWorkTimeTotal().intValue(), is(220 * 60));
        assertThat(result.getWorkTimeHoliday().intValue(), is(40 * 60));
        assertThat(result.getWorkTimeLateNight().intValue(), is(20 * 60));
        assertThat(result.getWorkingHoursPlan().doubleValue(), is(80.0));
        assertThat(result.getAbsenceHour().doubleValue(), is(0.0));
        assertThat(result.getUnitPay().doubleValue(), is(100.0));
        assertThat(result.getBasePay().doubleValue(), is(10000.0));
        assertThat(result.getOvertimePayDeemed().doubleValue(), is(1000.0));
        assertThat(result.getOvertimePayEstimate().doubleValue(), is(18400.0));
        assertThat(result.getOvertimePay().doubleValue(), is(17400.0));
        assertThat(result.getMonthlySalary().doubleValue(), is(22400.0));
    }

}
