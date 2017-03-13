package com.framgia.attendance.test;

import java.util.List;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.unit.ConfigFileIncluder;
import org.seasar.framework.unit.TestContext;
import org.seasar.framework.util.tiger.CollectionsUtil;

public class ConfigFileIncluderImpl implements ConfigFileIncluder {

    protected static final Logger logger =
            Logger.getLogger(ConfigFileIncluderImpl.class);

    protected S2Container container;

    protected final List<String> configFiles = CollectionsUtil.newArrayList();

    @Binding(bindingType = BindingType.MUST)
    public void setContainer(S2Container container) {
        this.container = container;
    }

    public void addConfigFile(final String configFile) {
        configFiles.add(configFile);
    }

    public void include(final TestContext testContext) {
        for (final String configFile : configFiles) {
            include(testContext, configFile);
        }
    }

    protected void include(final TestContext testContext, final String path) {
        if (logger.isDebugEnabled()) {
            logger.log("DSSR0101", new Object[] { path });
        }
        S2ContainerFactory.include(container, path);
    }
}
