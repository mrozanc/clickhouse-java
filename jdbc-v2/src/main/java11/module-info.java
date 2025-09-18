/**
 * Declares com.clickhouse module.
 */
module com.clickhouse.jdbc {
    exports com.clickhouse.jdbc;

    requires java.sql;

//    requires transitive com.clickhouse.client;
    requires transitive com.clickhouse.client.api;
    requires static com.google.common;
    requires static org.antlr.antlr4.runtime;
    requires static org.slf4j;
    // requires transitive com.google.gson;
    // requires transitive org.lz4.java;

//    uses com.clickhouse.client.api.ClientConfigProperties;
//    uses com.clickhouse.client.ClickHouseClient;
//    uses com.clickhouse.client.ClickHouseDnsResolver;
//    uses com.clickhouse.client.ClickHouseSslContextProvider;
//    uses com.clickhouse.data.ClickHouseDataStreamFactory;
//    uses com.clickhouse.logging.LoggerFactory;
}
