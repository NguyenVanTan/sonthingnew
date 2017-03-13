package com.framgia.attendance.logic;

import static com.framgia.attendance.util.AttendanceConstant.ENCODE_DECODE_KEY;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.framgia.attendance.entity.Holiday;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;
import org.seasar.framework.container.annotation.tiger.Binding;

import com.framgia.attendance.dao.MonthDataDao;
import com.framgia.attendance.dao.RankDao;
import com.framgia.attendance.entity.Employee;
import com.framgia.attendance.entity.MonthData;
import com.framgia.attendance.entity.Rank;
import com.framgia.attendance.test.AttendanceWicketTest;
import com.framgia.attendance.util.MonthDataStatus;
import com.framgia.attendance.web.employee.YearMonth;

public class TimesheetListLogicTest extends AttendanceWicketTest {

    @Mocked public MonthDataDao monthDataDao;
    @Mocked public CalendarLogic calendarLogic;
    @Mocked public RankDao rankDao;
    @Mocked public Rank rank;
    @Binding private TimesheetListLogic timesheetListLogic;

    @Test
    public void testCheckFinalize_CheckFinalizeWhileHasOneMonthUnfinalizeBefore() throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        new Expectations() {
            {
                MonthData monthData = new MonthData();
                monthData.setEmployeeId(9);
                monthData.setYearMonthNum(201405);

                monthDataDao.countMonthDataBeforeSpecificMonthByStatus(monthData.getEmployeeId(),
                        monthData.getYearMonthNum(),
                        MonthDataStatus.FINALIZED.getValue());
                result = 1;
            }
        };

        Deencapsulation.setField(timesheetListLogic, monthDataDao);
        MonthData monthData = new MonthData();
        monthData.setEmployeeId(9);
        monthData.setYearMonthNum(201405);

        boolean result = timesheetListLogic.checkFinalize(monthData);
        assertThat(result, is(false));
    }

    @Test
    public void testCheckFinalize_CheckFinalizeWhileHasNoAnyMonthUnfinalizeBefore() throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        new Expectations() {
            {
                MonthData monthData = new MonthData();
                monthData.setEmployeeId(9);
                monthData.setYearMonthNum(201406);
                monthDataDao.countMonthDataBeforeSpecificMonthByStatus(monthData.getEmployeeId(),
                        monthData.getYearMonthNum(),
                        MonthDataStatus.FINALIZED.getValue());
                result = 0;
            }
        };

        Deencapsulation.setField(timesheetListLogic, monthDataDao);
        MonthData monthData = new MonthData();
        monthData.setEmployeeId(9);
        monthData.setYearMonthNum(201406);

        boolean result = timesheetListLogic.checkFinalize(monthData);
        assertThat(result, is(true));
    }

    @Test
    public void testCheckUnFinalize_CheckUnfinalizeWhileHasOneMonthFinalizeAfter() throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        new Expectations() {
            {
                MonthData monthData = new MonthData();
                monthData.setEmployeeId(9);
                monthData.setYearMonthNum(201404);
                monthDataDao.countMonthDataAfterSpecificMonthByStatus(
                        monthData.getEmployeeId(),
                        monthData.getYearMonthNum(),
                        MonthDataStatus.FINALIZED.getValue());
                result = 1;
            }
        };

        Deencapsulation.setField(timesheetListLogic, monthDataDao);
        MonthData monthData = new MonthData();
        monthData.setEmployeeId(9);
        monthData.setYearMonthNum(201404);

        boolean result = timesheetListLogic.checkUnfinalize(monthData);
        assertThat(result, is(false));
    }

    @Test
    public void testCheckUnFinalize_CheckUnfinalizeWhileHasNoOneMonthFinalizeAfter() throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        new Expectations() {
            {
                MonthData monthData = new MonthData();
                monthData.setEmployeeId(9);
                monthData.setYearMonthNum(201405);
                monthDataDao.countMonthDataAfterSpecificMonthByStatus(
                        monthData.getEmployeeId(),
                        monthData.getYearMonthNum(),
                        MonthDataStatus.FINALIZED.getValue());
                result = 0;
            }
        };

        Deencapsulation.setField(timesheetListLogic, monthDataDao);
        MonthData monthData = new MonthData();
        monthData.setEmployeeId(9);
        monthData.setYearMonthNum(201405);

        boolean result = timesheetListLogic.checkUnfinalize(monthData);
        assertThat(result, is(true));
    }

    @Test
    public void testGetRequiredWorkTime_EmployeeDateEqualsSalaryMonthAndWorkingdayHour8() throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        new Expectations() {
            {
                calendarLogic.calcBusinessDayNum((YearMonth) any, anyInt, (List<Holiday>) any);
                result = 16;
            }
        };

        Deencapsulation.setField(timesheetListLogic, calendarLogic);
        Employee employee = new Employee();
        Calendar employeeDate = Calendar.getInstance();
        employeeDate.set(Calendar.YEAR, 2014);
        employeeDate.set(Calendar.MONTH, 2);
        employeeDate.set(Calendar.DAY_OF_MONTH, 15);
        java.sql.Date sDate = new java.sql.Date(employeeDate.getTime().getTime());
        employee.setEmploymentDate(sDate);

        MonthData monthData = new MonthData();
        Double workingdayHour = 8.0;
        monthData.setEmployeeId(9);
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201403);


        List<Holiday> holidays = new ArrayList<>();

        timesheetListLogic.getRequiredWorkTime(monthData, workingdayHour, holidays);
        assertThat(monthData.getWorkingDaysPlan(), is(16));
        assertThat(monthData.getWorkingHoursPlan(), is(16 * workingdayHour));
    }

    @Test
    public void testGetRequiredWorkTime_EmployeeDateEqualsSalaryMonthAndWorkingdayHour7() throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        new Expectations() {
            {
                calendarLogic.calcBusinessDayNum((YearMonth) any, anyInt, (List<Holiday>) any);
                result = 16;
            }
        };

        Deencapsulation.setField(timesheetListLogic, calendarLogic);
        Employee employee = new Employee();
        Calendar employeeDate = Calendar.getInstance();
        employeeDate.set(Calendar.YEAR, 2014);
        employeeDate.set(Calendar.MONTH, 2);
        employeeDate.set(Calendar.DAY_OF_MONTH, 15);
        java.sql.Date sDate = new java.sql.Date(employeeDate.getTime().getTime());
        employee.setEmploymentDate(sDate);

        MonthData monthData = new MonthData();
        Double workingdayHour = 7.0;
        monthData.setEmployeeId(9);
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201403);

        List<Holiday> holidays = new ArrayList<>();

        timesheetListLogic.getRequiredWorkTime(monthData, workingdayHour, holidays);
        assertThat(monthData.getWorkingDaysPlan(), is(16));
        assertThat(monthData.getWorkingHoursPlan(), is(16 * workingdayHour));
    }

    @Test
    public void testGetRequiredWorkTime_EmployeeDateNotEqualsSalaryMonthAndWorkingdayHour8() throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        new Expectations() {
            {
                calendarLogic.calcBusinessDayNum(new YearMonth(201403), (List<Holiday>) any);
                result = 20;
            }
        };

        Deencapsulation.setField(timesheetListLogic, calendarLogic);
        Employee employee = new Employee();
        employee.setEmployeeId(8);

        Calendar employeeDate = Calendar.getInstance();
        employeeDate.set(Calendar.YEAR, 2014);
        employeeDate.set(Calendar.MONTH, 1);
        employeeDate.set(Calendar.DAY_OF_MONTH, 2);
        java.sql.Date sDate = new java.sql.Date(employeeDate.getTime().getTime());
        employee.setEmploymentDate(sDate);

        MonthData monthData = new MonthData();
        Double workingdayHour = 8.0;
        monthData.setEmployeeId(8);
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201403);

        timesheetListLogic.getRequiredWorkTime(monthData, workingdayHour, new ArrayList<>());
        assertThat(monthData.getWorkingDaysPlan(), is(20));
        assertThat(monthData.getWorkingHoursPlan(), is(20 * workingdayHour));
    }

    @Test
    public void testGetRequiredWorkTime_EmployeeDateNotEqualsSalaryMonthAndWorkingdayHour7() throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        new Expectations() {
            {
                calendarLogic.calcBusinessDayNum(new YearMonth(201403), (List<Holiday>) any);
                result = 20;
            }
        };

        Deencapsulation.setField(timesheetListLogic, calendarLogic);
        Employee employee = new Employee();
        employee.setEmployeeId(8);

        Calendar employeeDate = Calendar.getInstance();
        employeeDate.set(Calendar.YEAR, 2014);
        employeeDate.set(Calendar.MONTH, 1);
        employeeDate.set(Calendar.DAY_OF_MONTH, 2);
        java.sql.Date sDate = new java.sql.Date(employeeDate.getTime().getTime());
        employee.setEmploymentDate(sDate);

        MonthData monthData = new MonthData();
        Double workingdayHour = 7.0;
        monthData.setEmployeeId(8);
        monthData.setEmployee(employee);
        monthData.setYearMonthNum(201403);

        timesheetListLogic.getRequiredWorkTime(monthData, workingdayHour, new ArrayList<>());
        assertThat(monthData.getWorkingDaysPlan(), is(20));
        assertThat(monthData.getWorkingHoursPlan(), is(20 * workingdayHour));
    }

    @Test
    public void testResetDataFieldsToUnfinalize() {
        final MonthData monthData = new MonthData();
        final Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        monthData.setMonthDataStatus(MonthDataStatus.APPROVED);
        monthData.setBackofficeFixDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        monthData.setBackofficeUserId(123);
        monthData.setPayVacationLeftJoinBeforefinalize(1.0);
        monthData.setPayVacationLeftJoinTempBeforefinalize(2.0);
        monthData.setPayVacationLeftLastYearBeforefinalize(3.0);
        monthData.setPayVacationLeftThisYearBeforefinalize(4.0);
        monthData.setAbsenceHour(1.0);
        monthData.setBasePay(1.0);
        monthData.setUnitPay(1.0);
        monthData.setOvertimePayDeemed(1.0);
        monthData.setOvertimePayEstimate(1.0);
        monthData.setOvertimePay(10.0);
        monthData.setMonthlySalary(10.0);
        monthData.setUpdTimestamp(today);

        new Expectations(timesheetListLogic) {
            {
                Deencapsulation.invoke(timesheetListLogic, "resetDataFieldsToUnfinalize", monthData);
                monthData.setBackofficeFixDate(null);
                monthData.setBackofficeUserId(null);
                monthData.setPayVacationLeftJoinBeforefinalize(null);
                monthData.setPayVacationLeftJoinTempBeforefinalize(null);
                monthData.setPayVacationLeftLastYearBeforefinalize(null);
                monthData.setPayVacationLeftThisYearBeforefinalize(null);
                monthData.setAbsenceHour(0.0);
                monthData.setBasePay(null);
                monthData.setUnitPay(null);
                monthData.setOvertimePayDeemed(0.0);
                monthData.setOvertimePayEstimate(0.0);
                monthData.setOvertimePay(null);
                monthData.setMonthlySalary(null);
                monthData.setUpdTimestamp(today);
                result = null;
            }
        };

        timesheetListLogic.updateMonthDataAndEmployee(monthData);
        Date dateNull = null;
        Double doubleNull = null;
        Integer IntNull = null;
        assertThat(monthData.getBackofficeFixDate(), is(dateNull));
        assertThat(monthData.getBackofficeUserId(), is(IntNull));
        assertThat(monthData.getPayVacationLeftJoinBeforefinalize(), is(doubleNull));
        assertThat(monthData.getPayVacationLeftJoinTempBeforefinalize(), is(doubleNull));
        assertThat(monthData.getPayVacationLeftLastYearBeforefinalize(), is(doubleNull));
        assertThat(monthData.getPayVacationLeftThisYearBeforefinalize(), is(doubleNull));
        assertThat(monthData.getAbsenceHour(), is(0.0));
        assertThat(monthData.getBasePay(), is(doubleNull));
        assertThat(monthData.getUnitPay(), is(doubleNull));
        assertThat(monthData.getOvertimePayDeemed(), is(0.0));
        assertThat(monthData.getOvertimePayEstimate(), is(0.0));
        assertThat(monthData.getOvertimePay(), is(doubleNull));
        assertThat(monthData.getMonthlySalary(), is(doubleNull));
        assertEquals(monthData.getUpdTimestamp(), today);
    }

    @Test
    public void testGetMonthlyWorkingHoursPercent_withFullMonthData() {
        final Employee employee = new Employee();
        employee.setEmployeeId(100);
        final int yearMonth = 201410;
        final String key = "UZABASEATTENDANCE";
        new Expectations() {
            {
                MonthData returnMonthData = new MonthData();
                returnMonthData.setEmployeeId(100);
                returnMonthData.setYearMonthNum(yearMonth);
                returnMonthData.setWorkingDaysPlan(21);
                returnMonthData.setWorkTimeTotal(21 * 8 * 60);
                monthDataDao.getMonthData(employee.getEmployeeId(), yearMonth, ENCODE_DECODE_KEY);
                result = returnMonthData;
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
            }
        };
        Deencapsulation.setField(timesheetListLogic, monthDataDao);
        Deencapsulation.setField(timesheetListLogic, "rankDao", rankDao);

        double workingHoursPercent = timesheetListLogic.getMonthlyWorkingHoursPercent(employee, yearMonth);
        assertThat(workingHoursPercent, is(1.0));
    }

    @Test
    public void testGetMonthlyWorkingHoursPercent_withHalpMonthData() {
        final Employee employee = new Employee();
        employee.setEmployeeId(100);
        final int yearMonth = 201410;
        final String key = "UZABASEATTENDANCE";
        new Expectations() {
            {
                MonthData returnMonthData = new MonthData();
                returnMonthData.setEmployeeId(100);
                returnMonthData.setYearMonthNum(yearMonth);
                returnMonthData.setWorkingDaysPlan(22);
                returnMonthData.setWorkTimeTotal(11 * 8 * 60);
                monthDataDao.getMonthData(employee.getEmployeeId(), yearMonth, ENCODE_DECODE_KEY);
                result = returnMonthData;
                rankDao.selectByRankCd(anyString);
                result = rank;
                rank.getWorkHoursPerDay();
                result = 8.0;
            }
        };
        Deencapsulation.setField(timesheetListLogic, monthDataDao);
        Deencapsulation.setField(timesheetListLogic, "rankDao", rankDao);

        double workingHoursPercent = timesheetListLogic.getMonthlyWorkingHoursPercent(employee, yearMonth);
        assertThat(workingHoursPercent, is(0.5));
    }

    @Test
    public void testGetMonthlyWorkingHoursPercent_withNoMonthData() {
        final Employee employee = new Employee();
        employee.setEmployeeId(100);
        final int yearMonth = 201411;
        final String key = "UZABASEATTENDANCE";
        new Expectations() {
            {
                MonthData returnMonthData = new MonthData();
                returnMonthData.setEmployeeId(100);
                returnMonthData.setYearMonthNum(yearMonth);
                returnMonthData.setWorkingDaysPlan(22);
                returnMonthData.setWorkTimeTotal(0);
                monthDataDao.getMonthData(employee.getEmployeeId(), yearMonth, ENCODE_DECODE_KEY);
                result = null;
            }
        };
        Deencapsulation.setField(timesheetListLogic, monthDataDao);

        double workingHoursPercent = timesheetListLogic.getMonthlyWorkingHoursPercent(employee, yearMonth);
        assertThat(workingHoursPercent, is(0.0));
    }
}
