plugins {
    `java-conventions`
}

java {
    registerFeature("compression") {
        usingSourceSet(sourceSets.main.get())
    }
}

dependencies {
    api(project(":clickhouse-client"))
    api(libs.httpcomponents.client5.httpclient5)

    "compressionImplementation"(libs.zstd.jni)
    "compressionImplementation"(libs.commons.compress)
    "compressionImplementation"(libs.brotli.dec)
    "compressionImplementation"(libs.lz4.pure.java)
    "compressionImplementation"(libs.xz)

    java11CompileOnly(libs.zstd.jni)
    java11CompileOnly(libs.commons.compress)
    java11CompileOnly(libs.brotli.dec)
    java11CompileOnly(libs.lz4.pure.java)
    java11CompileOnly(libs.xz)

    annotationProcessor(libs.tomcat.annotations.api)

    testImplementation(libs.testcontainers.toxiproxy)
    testImplementation(libs.wiremock.standalone)
    testImplementation(testFixtures(project(":clickhouse-client")))
}
