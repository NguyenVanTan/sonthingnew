package com.framgia.attendance.logic;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class ProjectLogicTest {

    @Binding
    ProjectLogic projectLogic;

    @Test
    public void getPercentageList1() {
        assertThat(projectLogic.getPercentageList().size(), comparesEqualTo(10));
    }

    @Test
    public void getPercentageList2() {
        assertThat(projectLogic.getPercentageList().get(0),
                comparesEqualTo(100));
    }

    @Test
    public void getPercentageList3() {
        assertThat(projectLogic.getPercentageList().get(9), comparesEqualTo(10));
    }

}
