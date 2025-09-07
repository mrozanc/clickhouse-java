package com.clickhouse.test;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple soft assertion mechanism that also captures the stacktrace to help pin point the source
 * of failure.
 */
public class SoftAssertWithLineNumber extends Assertion {

    private static final String SELF_CLASS_NAME = SoftAssertWithLineNumber.class.getCanonicalName();

    private final Map<AssertionError, IAssert<?>> m_errors = new LinkedHashMap<>();

    @Override
    protected void doAssert(IAssert<?> a) {
        onBeforeAssert(a);
        try {
            a.doAssert();
            onAssertSuccess(a);
        } catch (AssertionError ex) {
            onAssertFailure(a, ex);
            m_errors.put(ex, a);
        } finally {
            onAfterAssert(a);
        }
    }

    public void assertAll() {
        if (!m_errors.isEmpty()) {
            StringBuilder sb = new StringBuilder("The following asserts failed:");
            boolean first = true;
            for (Map.Entry<AssertionError, IAssert<?>> ae : m_errors.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append("\n\t");
                sb.append(ae.getKey().getMessage());
                sb.append(" at: ");
                sb.append(findLocationInStackTrace(ae.getKey().getStackTrace()));
            }
            throw new AssertionError(sb.toString());
        }
    }

    private static StackTraceElement findLocationInStackTrace(StackTraceElement[] stackTrace) {
        for (int i = 0; i < stackTrace.length; ++i) {
            StackTraceElement ste = stackTrace[i];
            if (SELF_CLASS_NAME.equals(ste.getClassName()) && "doAssert".equals(ste.getMethodName())) {
                return stackTrace[i + 2];
            }
        }
        return null;
    }
}
