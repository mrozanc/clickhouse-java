rootProject.name = "clickhouse-java"

// Include only Maven modules; ignore .github, examples, and performance
include("clickhouse-data")
include("clickhouse-client")
include("clickhouse-http-client")
include("client-v2")
include("clickhouse-jdbc")
include("jdbc-v2")
include("clickhouse-r2dbc")
