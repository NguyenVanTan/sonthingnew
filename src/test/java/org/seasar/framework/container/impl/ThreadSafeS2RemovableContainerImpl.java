package org.seasar.framework.container.impl;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.ComponentDef;

public class ThreadSafeS2RemovableContainerImpl extends
        ThreadSafeS2ContainerImpl {
    private final Field componentDefMapField;
    private final Field componentDefListField;

    public ThreadSafeS2RemovableContainerImpl() {
        try {
            componentDefMapField =
                    S2ContainerImpl.class.getDeclaredField("componentDefMap");
            componentDefListField =
                    S2ContainerImpl.class.getDeclaredField("componentDefList");

            componentDefMapField.setAccessible(true);
            componentDefListField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        } catch (SecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void unregisterMap(ComponentDef componentDef) {
        try {
            Map<Object, ComponentDefHolder> componentDefMap =
                    (Map<Object, ComponentDefHolder>) componentDefMapField.get(this);
            List<ComponentDef> componentDefList =
                    (List<ComponentDef>) componentDefListField.get(this);

            for (Iterator<ComponentDefHolder> itr =
                    componentDefMap.values().iterator(); itr.hasNext();) {
                // インスタンスの同等性を比較して削除
                if (itr.next().getComponentDef() == componentDef) {
                    itr.remove();
                }
            }

            boolean exists = componentDefList.remove(componentDef);

            if (exists) {
                unregisterParent(componentDef);
                unregisterChild(componentDef);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void unregisterParent(ComponentDef componentDef) {
        for (int i = 0; i < getParentSize(); i++) {
            if (getParent(i) instanceof ThreadSafeS2RemovableContainerImpl) {
                ThreadSafeS2RemovableContainerImpl parent =
                        (ThreadSafeS2RemovableContainerImpl) getParent(i);
                parent.unregisterMap(componentDef);
            }
        }
    }

    protected void unregisterChild(ComponentDef componentDef) {
        for (int i = 0; i < getChildSize(); i++) {
            if (getChild(i) instanceof ThreadSafeS2RemovableContainerImpl) {
                ThreadSafeS2RemovableContainerImpl parent =
                        (ThreadSafeS2RemovableContainerImpl) getChild(i);
                parent.unregisterMap(componentDef);
            }
        }
    }
}
