plugins {
    `java-conventions`
}

dependencies {
    api(project(":clickhouse-data"))
    implementation(libs.roaringbitmap)
    api(project(":clickhouse-client"))
    implementation(libs.slf4j.api)
    implementation(libs.httpcomponents.client5.httpclient5)
    implementation(libs.lz4.java)
    implementation(libs.commons.compress)
    implementation(libs.asm)
    compileOnly(libs.micrometer.core)
    implementation(libs.guava)

    testImplementation(libs.micrometer.core)
    testImplementation(libs.jackson.databind)
    testImplementation(libs.testcontainers.toxiproxy)
    testImplementation(libs.wiremock.standalone)
    testImplementation(libs.mockito.core)
    testImplementation(testFixtures(project(":clickhouse-client")))
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}
