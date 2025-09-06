module com.clickhouse.client.http {
    exports com.clickhouse.client.http;
    exports com.clickhouse.client.http.config;

    provides com.clickhouse.client.ClickHouseClient with com.clickhouse.client.http.ClickHouseHttpClient;

    requires java.net.http;
    requires org.apache.httpcomponents.client5.httpclient5;

    requires transitive com.clickhouse.client;
}
