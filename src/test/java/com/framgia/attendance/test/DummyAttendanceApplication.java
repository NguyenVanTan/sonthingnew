package com.framgia.attendance.test;

import org.apache.wicket.Session;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.seasar.wicket.ComponentInjectionListener;

import com.framgia.attendance.web.AttendanceWebSession;

public class DummyAttendanceApplication extends MockApplication {
	@Override
	protected void init() {
		super.init();
		getComponentInstantiationListeners().add(
				new ComponentInjectionListener());

	}


	@Override
	public Session newSession(Request request, Response response) {
		return new AttendanceWebSession(request);
	}
}
