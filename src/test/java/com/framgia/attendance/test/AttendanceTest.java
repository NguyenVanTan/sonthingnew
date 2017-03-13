package com.framgia.attendance.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.framework.unit.Seasar2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Seasar2.class)
public abstract class AttendanceTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public static boolean compareLists(List<?> list1, List<?> list2) {

        assertEquals(list1.size(), list2.size());
        for (int i = 0; i < list1.size(); i++) {
            compareObjects(list1.get(i), list2.get(i));
        }
        return true;
    }

    public static boolean compareObjects(Object o1, Object o2) {
        Map<String, ?> newDto = new HashMap<>();
        Map<String, ?> oldDto = new HashMap<>();
        BeanUtil.copyProperties(o1, newDto);
        BeanUtil.copyProperties(o2, oldDto);
        for (String key : newDto.keySet()) {
            Object value1 = newDto.get(key);
            Object value2 = oldDto.get(key);
            if (!((value1 == null && value2 == null) || (value1 != null && value2.toString().equals(
                    value2.toString())))) {
                System.out.println(key + " : " + value1 + " / " + value2);
                fail();
            }
        }
        return true;
    }
}
