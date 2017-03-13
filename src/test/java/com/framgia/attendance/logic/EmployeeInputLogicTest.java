package com.framgia.attendance.logic;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Calendar;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import com.framgia.attendance.entity.Employee;
import com.framgia.attendance.entity.UserAccount;
import com.framgia.attendance.test.AttendanceWicketTest;

public class EmployeeInputLogicTest extends AttendanceWicketTest {
	
	@Mocked
	EmployeeInputLogic employeeInputLogic;
	
	@Test
    public void testResetPayVacationLeft_ResetPayVacationLeftJoin(){
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        final Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setEmploymentDate(new java.sql.Date(cal.getTime().getTime()));
        employee.setPayVacationLeftJoin(5.0);
        employee.setPayVacationLeftJoinTemp(null);
        employee.setPayVacationLeftJoinTempDate(null);
        
        employee.setPayVacationLeftLastYear(11.0);
        cal.set(Calendar.YEAR, 2013);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));
        
        employee.setPayVacationLeftThisYear(12.0);
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));


        final UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1);
        userAccount.setEmployeeId(1);
        new Expectations() {
            {
                employeeInputLogic
                        .resetPayVacationLeft(userAccount);
                
                employee.setPayVacationLeftJoin(0.0);
                
                returns(void.class);
            }
        };
        
        employeeInputLogic.resetPayVacationLeft(userAccount);

        assertThat(employee.getPayVacationLeftJoin(), is(0.0));
    }
    
    @Test
    public void testResetPayVacationLeft_ResetPayVacationLeftJoinTemp(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        final Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setEmploymentDate(new java.sql.Date(cal.getTime().getTime()));
        employee.setPayVacationLeftJoin(0.0);

        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 2);
        employee.setPayVacationLeftJoinTemp(10.0);
        employee.setPayVacationLeftJoinTempDate(new java.sql.Date(cal.getTime().getTime()));
        
        employee.setPayVacationLeftLastYear(11.0);
        cal.set(Calendar.YEAR, 2013);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));
        
        employee.setPayVacationLeftThisYear(12.0);
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));


        final UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1);
        userAccount.setEmployeeId(1);
        new Expectations() {
            {
                employeeInputLogic
                        .resetPayVacationLeft(userAccount);
                
                employee.setPayVacationLeftJoinTemp(0.0);
                
                returns(void.class);
            }
        };
        
        employeeInputLogic.resetPayVacationLeft(userAccount);

        assertThat(employee.getPayVacationLeftJoinTemp(), is(0.0));
    }
    
    @Test
    public void testResetPayVacationLeft_ResetPayVacationLeftLastYear(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        final Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setEmploymentDate(new java.sql.Date(cal.getTime().getTime()));
        employee.setPayVacationLeftJoin(0.0);
        
        employee.setPayVacationLeftJoinTemp(null);
        employee.setPayVacationLeftJoinTempDate(null);
        
        employee.setPayVacationLeftLastYear(11.0);
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));
        
        employee.setPayVacationLeftThisYear(12.0);
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));


        final UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1);
        userAccount.setEmployeeId(1);
        new Expectations() {
            {
                employeeInputLogic
                        .resetPayVacationLeft(userAccount);
                
                employee.setPayVacationLeftLastYear(0.0);
                
                returns(void.class);
            }
        };
        
        employeeInputLogic.resetPayVacationLeft(userAccount);

        assertThat(employee.getPayVacationLeftLastYear(), is(0.0));
    }
    
    @Test
    public void testResetPayVacationLeft_ResetPayVacationLeftThisYear(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        final Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setEmploymentDate(new java.sql.Date(cal.getTime().getTime()));
        employee.setPayVacationLeftJoin(0.0);
        
        employee.setPayVacationLeftJoinTemp(null);
        employee.setPayVacationLeftJoinTempDate(null);
        
        employee.setPayVacationLeftLastYear(null);
        employee.setPayVacationLeftLastYearDate(null);
        
        employee.setPayVacationLeftThisYear(12.0);
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));


        final UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1);
        userAccount.setEmployeeId(1);
        new Expectations() {
            {
                employeeInputLogic
                        .resetPayVacationLeft(userAccount);
                
                employee.setPayVacationLeftThisYear(0.0);
                
                returns(void.class);
            }
        };
        
        employeeInputLogic.resetPayVacationLeft(userAccount);

        assertThat(employee.getPayVacationLeftThisYear(), is(0.0));
    }
    
    @Test
    public void testResetPayVacationLeft_ResetAllPayVacationLeft(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        final Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setEmploymentDate(new java.sql.Date(cal.getTime().getTime()));
        employee.setPayVacationLeftJoin(5.0);
        
        employee.setPayVacationLeftJoinTemp(1.0);
        employee.setPayVacationLeftJoinTempDate(new java.sql.Date(cal.getTime().getTime()));
        
        employee.setPayVacationLeftLastYear(11.0);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));
        
        employee.setPayVacationLeftThisYear(12.0);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));


        final UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1);
        userAccount.setEmployeeId(1);
        new Expectations() {
            {
                employeeInputLogic
                        .resetPayVacationLeft(userAccount);
                
                employee.setPayVacationLeftJoin(0.0);
                employee.setPayVacationLeftJoinTemp(0.0);
                employee.setPayVacationLeftLastYear(0.0);
                employee.setPayVacationLeftThisYear(0.0);
                
                returns(void.class);
            }
        };
        
        employeeInputLogic.resetPayVacationLeft(userAccount);

        assertThat(employee.getPayVacationLeftJoin(), is(0.0));
        assertThat(employee.getPayVacationLeftJoinTemp(), is(0.0));
        assertThat(employee.getPayVacationLeftLastYear(), is(0.0));
        assertThat(employee.getPayVacationLeftThisYear(), is(0.0));
    }
    
    @Test
    public void testResetPayVacationLeft_NoResetAnyPayVacationLeft(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        final Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setEmploymentDate(new java.sql.Date(cal.getTime().getTime()));
        employee.setPayVacationLeftJoin(5.0);
        
        employee.setPayVacationLeftJoinTemp(1.0);
        employee.setPayVacationLeftJoinTempDate(new java.sql.Date(cal.getTime().getTime()));
        
        employee.setPayVacationLeftLastYear(11.0);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));
        
        employee.setPayVacationLeftThisYear(12.0);
        employee.setPayVacationLeftLastYearDate(new java.sql.Date(cal.getTime().getTime()));


        final UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1);
        userAccount.setEmployeeId(1);
        new Expectations() {
            {
                employeeInputLogic
                        .resetPayVacationLeft(userAccount);
                
                returns(void.class);
            }
        };
        
        employeeInputLogic.resetPayVacationLeft(userAccount);

        assertThat(employee.getPayVacationLeftJoin(), is(5.0));
        assertThat(employee.getPayVacationLeftJoinTemp(), is(1.0));
        assertThat(employee.getPayVacationLeftLastYear(), is(11.0));
        assertThat(employee.getPayVacationLeftThisYear(), is(12.0));
    }
}
