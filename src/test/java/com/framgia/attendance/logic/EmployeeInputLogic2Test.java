package com.framgia.attendance.logic;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static com.framgia.attendance.util.AttendanceConstant.ENCODE_DECODE_KEY;

import java.util.Calendar;
import java.util.List;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Before;
import org.junit.Test;
import org.seasar.framework.container.annotation.tiger.Binding;

import com.framgia.attendance.dao.DayDataDao;
import com.framgia.attendance.dao.EmployeeDao;
import com.framgia.attendance.dao.MonthDataDao;
import com.framgia.attendance.dao.RankDao;
import com.framgia.attendance.entity.DayData;
import com.framgia.attendance.entity.Employee;
import com.framgia.attendance.entity.MonthData;
import com.framgia.attendance.entity.Rank;
import com.framgia.attendance.test.AttendanceWicketTest;
import com.framgia.attendance.util.DayType;
import com.framgia.attendance.util.MonthDataStatus;


public class EmployeeInputLogic2Test extends AttendanceWicketTest {
    @Mocked
    RankDao rankDao;

    @Mocked
    DayDataDao dayDataDao;
    
    @Mocked
    EmployeeDao employeeDao;
    
    @Mocked
    MonthDataDao monthDataDao;

    @Binding
    EmployeeInputLogic employeeInputLogic;
    
    private final String msgFromDateProcess = "employeePanel.msg.editTransferFromDate.invalid";
    
    private final String msgToDateProcess = "employeePanel.msg.editTransferToDate.invalid";

    private final String rankCode = "rankCode";
    private final int employeeId = 123;
    
    @Before
    public void init()
    {
       new NonStrictExpectations() {{  
           Rank rank = new Rank();
           rank.setWorkHoursPerDay(6d);
           rankDao.selectByRankCd(anyString);
           result = rank;
       }};
    }
    

    @Test
    public void testCreateAutoTimesheetForTransferEmployee_whenTimesheetNotYetSubscribe()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 7, 8);

        final Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 24);
        
        final Calendar joinDateCalendar = Calendar.getInstance();
        joinDateCalendar.set(2015, 5, 8);
        
        final int  yearMonthTo = employeeInputLogic.getYearMonthOfSpecificDate(toCalendar.getTime());
        

        new NonStrictExpectations() {
            {
                Calendar calendarOldFrom = Calendar.getInstance();
                calendarOldFrom.set(2015, 7, 6);
               
                Employee employee = new Employee();
                
                employee.setEmployeeId(123);
                employee.setTransferFromDate(calendarOldFrom.getTime());
                employee.setTransferToDate(toCalendar.getTime());
                employee.setEmploymentDate(joinDateCalendar.getTime());
                
                employeeDao.getEmployeeById(employee.getEmployeeId(), anyString);
                result = employee;
                
                MonthData monthDataTo = new MonthData();
                monthDataTo.setStatus(MonthDataStatus.UNAPPLIED.getValue());
                monthDataTo.setYearMonthNum(yearMonthTo);
                
                monthDataDao.getMonthData(anyInt, anyInt, anyString);
                result = monthDataTo;
                
                dayDataDao.getDayData(employeeId, anyInt, anyInt);
                result = null;

                dayDataDao.insertBatch((List<DayData>) any);

                dayDataDao.updateBatch((List<DayData>) any);
            }
        };

        Deencapsulation.setField(employeeInputLogic, rankDao);
        Deencapsulation.setField(employeeInputLogic, monthDataDao);
        Deencapsulation.setField(employeeInputLogic, dayDataDao);
        Deencapsulation.setField(employeeInputLogic, employeeDao);
        
        employeeInputLogic.createAutoTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);

        new Verifications()
        {
            {
                dayDataDao.insertBatch((List<DayData>) any);
                times = 1;
                
                dayDataDao.updateBatch((List<DayData>) any);
                times = 0;
            }
        };
    }
    
    
    @Test
    public void testCreateAutoTimesheetForTransferEmployee_whenTimesheetCreated()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 7, 8);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 24);

        new NonStrictExpectations() {
            {
                
                DayData dayData = new DayData(employeeId, 201508, 8, DayType.WEEKDAY.getValue());
                
                dayDataDao.getDayData(anyInt, anyInt, anyInt);
                result = dayData;

                dayDataDao.insertBatch((List<DayData>) any);
                result = anyInt;

                dayDataDao.updateBatch((List<DayData>) any);
                result = anyInt;
            }
        };

        Deencapsulation.setField(employeeInputLogic, rankDao);
        Deencapsulation.setField(employeeInputLogic, dayDataDao);
        
        employeeInputLogic.createAutoTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);

        new Verifications()
        {
            {
                dayDataDao.insertBatch((List<DayData>) any);
                times = 0;
                
                dayDataDao.updateBatch((List<DayData>) any);
                times = 1;
            }
        };
    }
    
    
    @Test
    public void testCreateAutoTimesheetForTransferEmployee_whenSomedaySubcribeSomedayNotSubcribe()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 7, 8);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 24);

        new NonStrictExpectations() {
            {
                
                DayData dayDataSubcribe = new DayData(employeeId, 201508, 9, DayType.WEEKDAY.getValue());
                
                dayDataDao.getDayData(employeeId, 201508, 9);
                result = dayDataSubcribe;

                dayDataDao.insertBatch((List<DayData>) any);
                result = anyInt;

                dayDataDao.updateBatch((List<DayData>) any);
                result = anyInt;
            }
        };

        Deencapsulation.setField(employeeInputLogic, rankDao);
        Deencapsulation.setField(employeeInputLogic, dayDataDao);
        
        employeeInputLogic.createAutoTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);

        new Verifications()
        {
            {
                dayDataDao.insertBatch((List<DayData>) any);
                times = 1;
                
                dayDataDao.updateBatch((List<DayData>) any);
                times = 1;
            }
        };
    }
    
    @Test
    public void testGetYearMonthOfSpecificDate_withMonthLessThan10()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 7, 8);
        int yearMonth = employeeInputLogic.getYearMonthOfSpecificDate(fromCalendar.getTime());
        
        assertThat(yearMonth, is(201508));
        
        
    }
    
    @Test
    public void testGetYearMonthOfSpecificDate_withMonthGreaterThan10()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 11, 11);
        int yearMonth = employeeInputLogic.getYearMonthOfSpecificDate(fromCalendar.getTime());
        
        assertThat(yearMonth, is(201512));
    }
    
    
    @Test
    public void testUpdateTimesheetForTransferEmployee_whenEditFromDateInMonthWhichIsSendApporveAndEmployeeNoTransferBefore()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 7, 8);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 24);
        
        new NonStrictExpectations() {
            {
                Employee employee = new Employee();
                
                employeeDao.getEmployeeByEmployeeId(anyInt);
                result = employee;
                
                MonthData monthDataFrom = new MonthData();
                monthDataFrom.setStatus(MonthDataStatus.APPLIED.getValue());
                
                monthDataDao.getMonthDataByEmployeeIdAndYearMonth(anyInt, anyInt, ENCODE_DECODE_KEY);
                result = monthDataFrom;
                
            }
        };
        
        Deencapsulation.setField(employeeInputLogic, employeeDao);
        Deencapsulation.setField(employeeInputLogic, monthDataDao);
        
        String resultStr = employeeInputLogic.updateTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);
        
        assertEquals(msgFromDateProcess, resultStr);
    }
    
    
    @Test
    public void testUpdateTimesheetForTransferEmployee_whenEditToDateInMonthWhichIsSendApporveAndEmployeeNoTransferBefore()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 6, 8);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 24);
        
        final int  yearMonthTo = employeeInputLogic.getYearMonthOfSpecificDate(toCalendar.getTime());
        
        new NonStrictExpectations() {
            {
                Employee employee = new Employee();
                
                employeeDao.getEmployeeByEmployeeId(anyInt);
                result = employee;
                
                MonthData monthDataTo = new MonthData();
                monthDataTo.setStatus(MonthDataStatus.APPLIED.getValue());
                
                monthDataDao.getMonthDataByEmployeeIdAndYearMonth(anyInt, yearMonthTo, ENCODE_DECODE_KEY);
                result = monthDataTo;
                
            }
        };
        
        Deencapsulation.setField(employeeInputLogic, employeeDao);
        Deencapsulation.setField(employeeInputLogic, monthDataDao);
        
        String resultStr = employeeInputLogic.updateTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);
        
        assertEquals(msgToDateProcess, resultStr);
    }
    
    
    @Test
    public void testUpdateTimesheetForTransferEmployee_whenEditFromDateInMonthWhichIsSendApporveAndEmployeeTransferBefore()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 6, 8);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 24);
        
        final int  yearMonthFrom = employeeInputLogic.getYearMonthOfSpecificDate(fromCalendar.getTime());
        
        new NonStrictExpectations() {
            {
                Calendar calendarOldFrom = Calendar.getInstance();
                calendarOldFrom.set(2015, 7, 6);
                  
                Calendar calendarOldTo = Calendar.getInstance();
                calendarOldTo.set(2015, 7, 15);
                
                Employee employee = new Employee();
                
                employee.setTransferFromDate(calendarOldFrom.getTime());
                employee.setTransferToDate(calendarOldTo.getTime());
                
                employeeDao.getEmployeeByEmployeeId(anyInt);
                result = employee;
                
                MonthData monthDataFrom = new MonthData();
                monthDataFrom.setStatus(MonthDataStatus.APPLIED.getValue());
                
                monthDataDao.getMonthDataByEmployeeIdAndYearMonth(anyInt, yearMonthFrom, ENCODE_DECODE_KEY);
                result = monthDataFrom;
                
            }
        };
        
        Deencapsulation.setField(employeeInputLogic, employeeDao);
        Deencapsulation.setField(employeeInputLogic, monthDataDao);
        
        String resultStr = employeeInputLogic.updateTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);
        
        assertEquals(msgFromDateProcess, resultStr);
    }
    
    @Test
    public void testUpdateTimesheetForTransferEmployee_whenEditToDateInMonthWhichIsSendApporveAndEmployeeTransferBefore()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 6, 8);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 24);
        
        final int  yearMonthTo = employeeInputLogic.getYearMonthOfSpecificDate(toCalendar.getTime());
        
        new NonStrictExpectations() {
            {
                Calendar calendarOldFrom = Calendar.getInstance();
                calendarOldFrom.set(2015, 6, 8);
                  
                Calendar calendarOldTo = Calendar.getInstance();
                calendarOldTo.set(2015, 7, 15);
                
                Employee employee = new Employee();
                employee.setEmployeeId(1234);
                employee.setTransferFromDate(calendarOldFrom.getTime());
                employee.setTransferToDate(calendarOldTo.getTime());
                
                employeeDao.getEmployeeByEmployeeId(anyInt);
                result = employee;
                
                MonthData monthDataTo = new MonthData();
                monthDataTo.setStatus(MonthDataStatus.APPLIED.getValue());
                
                monthDataDao.getMonthDataByEmployeeIdAndYearMonth(anyInt, yearMonthTo,ENCODE_DECODE_KEY);
                result = monthDataTo;
                
            }
        };
        
        Deencapsulation.setField(employeeInputLogic, employeeDao);
        Deencapsulation.setField(employeeInputLogic, monthDataDao);
        
        String resultStr = employeeInputLogic.updateTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);
        
        assertEquals(msgToDateProcess, resultStr);
    }
    
    
    @Test
    public void testUpdateTimesheetForTransferEmployee_whenExtendFromDateInMonthWhichIsNotSendApporve()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 7, 6);

        final Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 24);
        
        final Calendar joinDateCalendar = Calendar.getInstance();
        joinDateCalendar.set(2015, 5, 8);
        
        final int  yearMonthFrom = employeeInputLogic.getYearMonthOfSpecificDate(toCalendar.getTime());
        
        new NonStrictExpectations() {
            {
                Calendar calendarOldFrom = Calendar.getInstance();
                calendarOldFrom.set(2015, 7, 8);
                  
                Employee employee = new Employee();
                
                employee.setTransferFromDate(calendarOldFrom.getTime());
                employee.setTransferToDate(toCalendar.getTime());
                employee.setEmploymentDate(joinDateCalendar.getTime());
                
                employeeDao.getEmployeeByEmployeeId(anyInt);
                result = employee;
                
                MonthData monthDataFrom = new MonthData();                
                monthDataFrom.setEmployeeId(123);
                monthDataFrom.setStatus(MonthDataStatus.UNAPPLIED.getValue());
                monthDataFrom.setYearMonthNum(yearMonthFrom);
                
                monthDataDao.getMonthDataByEmployeeIdAndYearMonth(anyInt, yearMonthFrom, anyString);
                result = monthDataFrom;
                
                monthDataDao.getMonthData(anyInt, anyInt, anyString);
                result = monthDataFrom;
                
                dayDataDao.insertBatch((List<DayData>) any);
                result = anyInt;

                dayDataDao.updateBatch((List<DayData>) any);
                result = anyInt;
                
                employeeDao.getEmployeeById(employee.getEmployeeId(), anyString);
                result = employee;
                
            }
        };
        
        Deencapsulation.setField(employeeInputLogic, employeeDao);
        Deencapsulation.setField(employeeInputLogic, monthDataDao);
        Deencapsulation.setField(employeeInputLogic, dayDataDao);
        
        String resultStr = employeeInputLogic.updateTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);
        
        assertEquals(null, resultStr);
        
        
        new Verifications()
        {
            {
                List<DayData> newList;
                
                dayDataDao.insertBatch(newList = withCapture());
                times = 1;
                
                dayDataDao.updateBatch((List<DayData>) any);
                times = 0;
                
                int startTime = newList.get(0).getStartTime();
                assertEquals(800, startTime);

            }
        };
    }
    
    @Test
    public void testUpdateTimesheetForTransferEmployee_whenShortenFromDateInMonthWhichIsNotSendApporve()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 7, 8);
        
        final Calendar joinDateCalendar = Calendar.getInstance();
        joinDateCalendar.set(2015, 5, 8);

        final Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 24);
        
        final int  yearMonthFrom = employeeInputLogic.getYearMonthOfSpecificDate(toCalendar.getTime());
        
        new NonStrictExpectations() {
            {
                Calendar calendarOldFrom = Calendar.getInstance();
                calendarOldFrom.set(2015, 7, 6);
                
                
                Employee employee = new Employee();
                
                employee.setEmployeeId(123);
                employee.setTransferFromDate(calendarOldFrom.getTime());
                employee.setTransferToDate(toCalendar.getTime());
                employee.setEmploymentDate(joinDateCalendar.getTime());
                
                employeeDao.getEmployeeByEmployeeId(anyInt);
                result = employee;
                
                MonthData monthDataFrom = new MonthData();
                monthDataFrom.setEmployeeId(123);
                monthDataFrom.setStatus(MonthDataStatus.UNAPPLIED.getValue());
                monthDataFrom.setYearMonthNum(yearMonthFrom);
                
                monthDataDao.getMonthDataByEmployeeIdAndYearMonth(anyInt, yearMonthFrom, anyString);
                result = monthDataFrom;
                
                monthDataDao.getMonthData(anyInt, anyInt, anyString);
                result = monthDataFrom;
                
                dayDataDao.insertBatch((List<DayData>) any);
                result = anyInt;

                dayDataDao.updateBatch((List<DayData>) any);
                result = anyInt;
                
                employeeDao.getEmployeeById(employee.getEmployeeId(), anyString);
                result = employee;
                
            }
        };
        
        Deencapsulation.setField(employeeInputLogic, employeeDao);
        Deencapsulation.setField(employeeInputLogic, monthDataDao);
        Deencapsulation.setField(employeeInputLogic, dayDataDao);
        
        String resultStr = employeeInputLogic.updateTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);
        
        assertEquals(null, resultStr);
        
        new Verifications()
        {
            {
                List<DayData> newList;

                // Capture params to verify
                dayDataDao.insertBatch(newList = withCapture());
                times = 1;

                dayDataDao.updateBatch((List<DayData>) any);
                times = 0;

                assertEquals(null, newList.get(0).getWorkTimeTotal());
                assertEquals(null, newList.get(0).getStartTime());
                assertEquals(null, newList.get(0).getEndTime());
            }
        };
    }
    
    
    @Test
    public void testUpdateTimesheetForTransferEmployee_whenExtendToDateInMonthWhichIsNotSendApporve()
    {
        final Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 7, 6);
        
        final Calendar joinDateCalendar = Calendar.getInstance();
        joinDateCalendar.set(2015, 5, 8);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 26);
        
        final int  yearMonthTo = employeeInputLogic.getYearMonthOfSpecificDate(toCalendar.getTime());
        
        new NonStrictExpectations() {
            {
                  
                Calendar calendarOldTo = Calendar.getInstance();
                calendarOldTo.set(2015, 7, 24);
                
                Employee employee = new Employee();
                
                employee.setTransferFromDate(fromCalendar.getTime());
                employee.setTransferToDate(calendarOldTo.getTime());                
                employee.setEmployeeId(123);
                employee.setEmploymentDate(joinDateCalendar.getTime());
                
                employeeDao.getEmployeeByEmployeeId(anyInt);
                result = employee;
                
                MonthData monthDataTo = new MonthData();
                monthDataTo.setStatus(MonthDataStatus.UNAPPLIED.getValue());
                monthDataTo.setEmployeeId(123);              
                monthDataTo.setYearMonthNum(yearMonthTo);
                
                monthDataDao.getMonthDataByEmployeeIdAndYearMonth(anyInt, yearMonthTo, anyString);
                result = monthDataTo;
                
                monthDataDao.getMonthData(anyInt, anyInt, anyString);
                result = monthDataTo;
                
                
                dayDataDao.insertBatch((List<DayData>) any);
                result = anyInt;

                dayDataDao.updateBatch((List<DayData>) any);
                result = anyInt;
                
                employeeDao.getEmployeeById(employee.getEmployeeId(), anyString);
                result = employee;
                
            }
        };
        
        Deencapsulation.setField(employeeInputLogic, employeeDao);
        Deencapsulation.setField(employeeInputLogic, monthDataDao);
        Deencapsulation.setField(employeeInputLogic, dayDataDao);
        
        String resultStr = employeeInputLogic.updateTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);
        
        assertEquals(null, resultStr);
        
        new Verifications()
        {
            {
                List<DayData> newList;
                
                dayDataDao.insertBatch(newList = withCapture());
                times = 1;
                
                dayDataDao.updateBatch((List<DayData>) any);
                times = 0;
                
                int startTime = newList.get(0).getStartTime();
                assertEquals(800, startTime);

            }
        };
    }
    
    
    @Test
    public void testUpdateTimesheetForTransferEmployee_whenShortenToDateInMonthWhichIsNotSendApporve()
    {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(2015, 7, 6);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(2015, 7, 24);
        
        final Calendar joinDateCalendar = Calendar.getInstance();
        joinDateCalendar.set(2015, 5, 8);
        
        final int  yearMonthTo = employeeInputLogic.getYearMonthOfSpecificDate(toCalendar.getTime());
        
        new NonStrictExpectations() {
            {
                Calendar calendarOldFrom = Calendar.getInstance();
                calendarOldFrom.set(2015, 7, 6);
                  
                Calendar calendarOldTo = Calendar.getInstance();
                calendarOldTo.set(2015, 7, 26);
                
                Employee employee = new Employee();
                
                employee.setTransferFromDate(calendarOldFrom.getTime());
                employee.setTransferToDate(calendarOldTo.getTime());
                employee.setEmploymentDate(joinDateCalendar.getTime());
                
                employeeDao.getEmployeeByEmployeeId(anyInt);
                result = employee;
                
                MonthData monthDataTo = new MonthData();
                monthDataTo.setStatus(MonthDataStatus.UNAPPLIED.getValue());
                monthDataTo.setYearMonthNum(yearMonthTo);
                
                monthDataDao.getMonthDataByEmployeeIdAndYearMonth(anyInt, yearMonthTo, anyString);
                result = monthDataTo;
                
                monthDataDao.getMonthData(anyInt, anyInt, anyString);
                result = monthDataTo;
                
                dayDataDao.insertBatch((List<DayData>) any);
                result = anyInt;

                dayDataDao.updateBatch((List<DayData>) any);
                result = anyInt;
                
                employeeDao.getEmployeeById(employee.getEmployeeId(), anyString);
                result = employee;
                
            }
        };
        
        Deencapsulation.setField(employeeInputLogic, employeeDao);
        Deencapsulation.setField(employeeInputLogic, monthDataDao);
        Deencapsulation.setField(employeeInputLogic, dayDataDao);
        
        String resultStr = employeeInputLogic.updateTimesheetForTransferEmployee(fromCalendar.getTime(), toCalendar.getTime(), employeeId, rankCode);
        
        assertEquals(null, resultStr);
        
        new Verifications()
        {
            {
                List<DayData> newList;

                // Capture params to verify
                dayDataDao.insertBatch(newList = withCapture());
                times = 1;
                
                dayDataDao.updateBatch((List<DayData>) any);
                times = 0;

                
                assertEquals(null, newList.get(0).getWorkTimeTotal());
                assertEquals(null, newList.get(0).getStartTime());
                assertEquals(null, newList.get(0).getEndTime());
            }
        };
    }
}
