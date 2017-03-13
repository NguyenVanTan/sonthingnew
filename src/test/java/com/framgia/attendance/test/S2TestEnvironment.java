package com.framgia.attendance.test;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.unit.InternalTestContext;
import org.seasar.framework.unit.UnitClassLoader;

public class S2TestEnvironment {

    /** S2JUnit4のデフォルトの設定ファイルのパス */
    protected static final String DEFAULT_S2JUNIT4_PATH = "s2junit4.dicon";

    /** S2JUnit4の設定ファイルのパス */
    protected static String s2junit4Path = DEFAULT_S2JUNIT4_PATH;

    protected static ClassLoader originalClassLoader;
    protected static UnitClassLoader unitClassLoader;

    public static void setupTestContext() {
        originalClassLoader = getOriginalClassLoader();
        unitClassLoader = new UnitClassLoader(originalClassLoader);
        Thread.currentThread().setContextClassLoader(unitClassLoader);
        final S2Container container = createRootContainer();
        SingletonS2ContainerFactory.setContainer(container);

        InternalTestContext testContext =
                (InternalTestContext) container.getComponent(InternalTestContext.class);
        testContext.include();
        testContext.initContainer();
    }

    protected static ClassLoader getOriginalClassLoader() {
        S2Container configurationContainer =
                S2ContainerFactory.getConfigurationContainer();
        if (configurationContainer != null
                && configurationContainer.hasComponentDef(ClassLoader.class)) {
            return ClassLoader.class.cast(configurationContainer.getComponent(ClassLoader.class));
        }
        return Thread.currentThread().getContextClassLoader();
    }

    protected static S2Container createRootContainer() {
        return S2ContainerFactory.create(s2junit4Path);
    }

    protected static void tearDownTestContext() throws Throwable {
        S2ContainerBehavior.setProvider(new S2ContainerBehavior.DefaultProvider());
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        unitClassLoader = null;
        originalClassLoader = null;
    }
}
