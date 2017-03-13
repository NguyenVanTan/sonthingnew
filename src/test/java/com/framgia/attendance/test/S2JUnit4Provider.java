package com.framgia.attendance.test;

import org.junit.runner.Runner;
import org.seasar.framework.unit.S2Parameterized;
import org.seasar.framework.unit.S2TestClassRunner;
import org.seasar.framework.unit.Seasar2;

public class S2JUnit4Provider extends Seasar2.DefaultProvider {
    @Override
    public Runner createTestClassRunner(Class<?> clazz) throws Exception {
        if (hasParameterAnnotation(clazz)) {
            return new S2Parameterized(clazz);
        }
        return new S2TestClassRunner(clazz, new S2JUnit4TestClassMethodsRunner(
                clazz));
    }
}
