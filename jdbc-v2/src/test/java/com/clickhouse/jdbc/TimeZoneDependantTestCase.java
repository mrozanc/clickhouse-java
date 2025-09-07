package com.clickhouse.jdbc;

import java.util.TimeZone;

public final class TimeZoneDependantTestCase {

    public interface TestCaseRunnable {
        void run() throws Exception;
    }

    public static void executeWithDefaultTimeZone(String defaultTimeZoneId, TestCaseRunnable runnable) {
        final TimeZone originalDefaultTimeZone = TimeZone.getDefault();
        try {
            if (defaultTimeZoneId != null) {
                TimeZone.setDefault(TimeZone.getTimeZone(defaultTimeZoneId));
            }
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (defaultTimeZoneId != null) {
                TimeZone.setDefault(originalDefaultTimeZone);
            }
        }
    }

    private TimeZoneDependantTestCase() {
    }
}
