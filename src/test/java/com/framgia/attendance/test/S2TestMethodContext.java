package com.framgia.attendance.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.ThreadSafeS2RemovableContainerImpl;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.unit.InternalTestContext;
import org.seasar.framework.unit.S2TestIntrospector;
import org.seasar.framework.unit.S2TestMethodRunner;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.PublishedTestContext;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

public class S2TestMethodContext extends S2TestMethodRunner {

    static {
        S2TestEnvironment.setupTestContext();
    }

    public S2TestMethodContext(Object test, Method method,
            RunNotifier notifier, Description description,
            S2TestIntrospector introspector) {
        super(test, method, notifier, description, introspector);
    }

    @Override
    public void setUpTestContext() throws Throwable {
        S2Container container = SingletonS2ContainerFactory.getContainer();

        testContext =
                (InternalTestContext) container.getComponent(InternalTestContext.class);
        testContext.setTestClass(testClass);
        testContext.setTestMethod(method);
        if (!testContext.hasComponentDef(NamingConvention.class)
                && introspector.isRegisterNamingConvention(testClass, method)) {
            final NamingConvention namingConvention =
                    new NamingConventionImpl();
            testContext.register(namingConvention);
            testContext.setNamingConvention(namingConvention);
        }

        for (Class<?> clazz = testClass; clazz != Object.class; clazz =
                clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                Class<?> fieldClass = field.getType();
                if (isAutoBindable(field)
                        && fieldClass.isAssignableFrom(testContext.getClass())
                        && fieldClass.isAnnotationPresent(PublishedTestContext.class)) {
                    field.setAccessible(true);
                    if (ReflectionUtil.getValue(field, test) != null) {
                        continue;
                    }
                    bindField(field, testContext);
                }
            }
        }
    }

    @Override
    public void tearDownTestContext() throws Throwable {
        testContext = null;
        DisposableUtil.dispose();
    }

    public void bindEasyMock() {
        unregisterEasyMockField();
        easyMockSupport.bindMockFields(test, testContext.getContainer());
        introspector.createMock(method, test, testContext);
    }

    public void unbindEasyMock() {
        easyMockSupport.unbindMockFields(test);
        unregisterEasyMockField();
    }

    private void unregisterEasyMockField() {
        S2Container container = testContext.getContainer();
        if (!(container instanceof ThreadSafeS2RemovableContainerImpl)) {
            return;
        }

        ThreadSafeS2RemovableContainerImpl removable =
                (ThreadSafeS2RemovableContainerImpl) container;
        for (Class<?> clazz = test.getClass(); clazz != Object.class; clazz =
                clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                EasyMock annotation =
                        field.getAnnotation(org.seasar.framework.unit.annotation.EasyMock.class);
                if (annotation == null) {
                    continue;
                }

                ComponentDef componentDef =
                        removable.getComponentDef(field.getType());
                if (componentDef == null) {
                    continue;
                }

                removable.unregisterMap(componentDef);
            }
        }
    }

    @Override
    public void bindFields() throws Throwable {
        super.bindFields();
    }

    @Override
    public void unbindFields() throws Throwable {
        super.unbindFields();
    }

    @Override
    public boolean runEachRecord() throws Throwable {
        return super.runEachRecord();
    }

    @Override
    public void runTest() throws Throwable {
        super.runTest();
    }

    public void registerColumnTypes() {
        this.testContext.registerColumnTypes();
    }

    public void revertColumnTypes() {
        this.testContext.revertColumnTypes();
    }

    public void destroyContainer() {
        this.testContext.destroyContainer();
    }

    public void mockClear() {
        this.easyMockSupport.clear();
    }

    public void mockReplay() {
        this.easyMockSupport.replay();
    }

    public void mockVerify() {
        this.easyMockSupport.verify();
    }

    public void mockReset() {
        this.easyMockSupport.reset();
    }

    @Override
    protected void executeMethod() throws Throwable {
        try {
            this.executeMethodBody();
        } catch (InvocationTargetException e) {
            final Throwable actual = e.getTargetException();
            throw actual;
        }
    }
}
