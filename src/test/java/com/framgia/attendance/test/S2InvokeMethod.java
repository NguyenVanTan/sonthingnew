package com.framgia.attendance.test;

import org.junit.runners.model.Statement;

public class S2InvokeMethod extends Statement {

    private S2TestMethodContext context;

    public S2InvokeMethod(S2TestMethodContext context) {
        this.context = context;
    }

    @Override
    public void evaluate() throws Throwable {
        context.mockClear();
        context.setUpTestContext();
        context.bindEasyMock();
        try {
            context.registerColumnTypes();
            try {
                context.bindFields();
                try {
                    final boolean recorded = context.runEachRecord();
                    if (recorded) {
                        context.mockReplay();
                    }
                    context.runTest();
                    if (recorded) {
                        context.mockVerify();
                        context.mockReset();
                    }
                } finally {
                    context.unbindFields();
                }
            } finally {
                context.revertColumnTypes();
            }
        } finally {
            context.unbindEasyMock();
        }
        context.tearDownTestContext();
    }
}
