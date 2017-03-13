package com.framgia.attendance.test;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.seasar.framework.unit.S2TestClassMethodsRunner;
import org.seasar.framework.unit.S2TestIntrospector;

public class S2JUnit4TestClassMethodsRunner extends BlockJUnit4ClassRunner {
    public interface ClassInfoProvider extends
            S2TestClassMethodsRunner.Provider {
        public S2TestIntrospector getIntrospector();
    }

    public static class DefaultClassInfoProvider extends
            S2TestClassMethodsRunner.DefaultProvider implements
            ClassInfoProvider {
        @Override
        public S2TestIntrospector getIntrospector() {
            return this.introspector;
        }
    }

    protected static ClassInfoProvider provider;

    protected static ClassInfoProvider getProvider() {
        if (provider == null) {
            provider = new DefaultClassInfoProvider();
        }
        return provider;
    }

    protected static void setProvider(final ClassInfoProvider p) {
        provider = p;
    }

    private RunNotifier currentNotifier;

    public S2JUnit4TestClassMethodsRunner(final Class<?> clazz)
            throws InitializationError {
        super(clazz);
    }

    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
        Description description = describeChild(method);
        if (method.getAnnotation(Ignore.class) != null) {
            notifier.fireTestIgnored(description);
        } else {
            this.currentNotifier = notifier;
            runLeaf(methodBlock(method), description, notifier);
        }
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        S2TestMethodContext methodContext =
                new S2TestMethodContext(test, method.getMethod(),
                        currentNotifier, methodDescription(method),
                        getProvider().getIntrospector());
        return new S2InvokeMethod(methodContext);
    }

    protected Description methodDescription(FrameworkMethod method) {
        return Description.createTestDescription(getTestClass().getJavaClass(),
                testName(method));
    }
}
