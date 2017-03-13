package com.framgia.attendance.test;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.WicketTester;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import com.framgia.attendance.web.AttendanceWebSession;

public class AttendanceWicketTester extends WicketTester {

	public AttendanceWicketTester() {
		
		super(new DummyAttendanceApplication());
	}

	@Override
	public AttendanceFormTester newFormTester(String path) {
		return newFormTester(path, true);
	}

	@Override
	public AttendanceFormTester newFormTester(String path,
			boolean fillBlankString) {
		return new AttendanceFormTester(path,
				(Form<?>) getComponentFromLastRenderedPage(path), this,
				fillBlankString);
	}

	public void executeAjaxBehavior(Component component) {
		List<? extends Behavior> behaviors = component.getBehaviors();
		for (Behavior behavior : behaviors) {
			executeBehavior((AbstractAjaxBehavior) behavior);
		}
	}

	public void loadAjaxLazyLoadPanel() {
		getLastRenderedPage().visitChildren(AjaxLazyLoadPanel.class,
				new IVisitor<AjaxLazyLoadPanel, Page>() {
					@Override
					public void component(AjaxLazyLoadPanel component,
							IVisit<Page> model) {
						loadAjaxLazyLoadPanel(component);
					}
				});
	}

	private void loadAjaxLazyLoadPanel(AjaxLazyLoadPanel panel) {
		List<? extends Behavior> behaviors = panel.getBehaviors();
		for (Behavior behavior : behaviors) {
			if (behavior instanceof AjaxSelfUpdatingTimerBehavior) {
				continue;
			}
			executeBehavior((AbstractAjaxBehavior) behavior);
		}
	}


	@Override
	public Session getSession() {
		return AttendanceWebSession.get();
	}

	public Panel startPanel(Panel panel) {
		return startComponentInPage(panel);
	}
}
