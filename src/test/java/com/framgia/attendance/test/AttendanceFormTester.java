package com.framgia.attendance.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.util.tester.BaseWicketTester;
import org.apache.wicket.util.tester.FormTester;

public class AttendanceFormTester extends FormTester {

    private final AttendanceWicketTester wicketTester;

    public AttendanceFormTester(String path, Form<?> workingForm,
            BaseWicketTester wicketTester, boolean fillBlankString) {
        super(path, workingForm, wicketTester, fillBlankString);
        this.wicketTester = (AttendanceWicketTester) wicketTester;
    }

    public void setValueAjax(String checkBoxId, boolean value) {
        super.setValue(checkBoxId, value);
        wicketTester.executeAjaxEvent(getForm().get(checkBoxId), "onclick");
    }

    public void selectAjax(String formComponentId, int index) {
        super.select(formComponentId, index);
        wicketTester.executeAjaxEvent(getForm().get(formComponentId),
                "onchange");
    }

    @SuppressWarnings("unchecked")
    public <T> void selectAjaxByDisplayValue(String formComponentId,
            String label) {
        AbstractSingleSelectChoice<T> component =
                (AbstractSingleSelectChoice<T>) getForm().get(formComponentId);

        int index = -1;
        IChoiceRenderer<? super T> renderer = component.getChoiceRenderer();
        List<? extends T> choices = component.getChoices();
        for (int i = 0; i < choices.size(); ++i) {
            String display =
                    renderer.getDisplayValue(choices.get(i)).toString();
            if (display.equals(label)) {
                index = i;
                break;
            }
        }

        assertThat(index, is(greaterThanOrEqualTo(0)));

        selectAjax(formComponentId, index);
    }
}
