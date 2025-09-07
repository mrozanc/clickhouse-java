/**
 * Declares com.clickhouse module.
 */
module com.clickhouse.client.api {
    exports com.clickhouse.client.api;
    exports com.clickhouse.client.api.data_formats;
    exports com.clickhouse.client.api.data_formats.internal;
    exports com.clickhouse.client.api.http;
    exports com.clickhouse.client.api.insert;
    exports com.clickhouse.client.api.internal;
    exports com.clickhouse.client.api.metadata;
    exports com.clickhouse.client.api.query;
    exports com.clickhouse.client.api.sql;

    requires transitive com.clickhouse.data;
    requires transitive com.clickhouse.client;

    requires static java.sql;
    requires static com.google.common;
    requires static org.apache.commons.compress;
    requires static org.apache.httpcomponents.core5.httpcore5;
    requires static org.objectweb.asm;
    requires static org.slf4j;
}
