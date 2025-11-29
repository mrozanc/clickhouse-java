plugins {
    `java-conventions`
    id("com.gradleup.shadow")
    `all-dependencies-jar`
}

dependencies {
    api(projects.clickhouseData)
    implementation(libs.roaringbitmap)
    api(projects.clickhouseClient)
    implementation(libs.slf4j.api)
    implementation(libs.httpcomponents.client5.httpclient5)
    implementation(libs.lz4.java)
    implementation(libs.commons.compress)
    implementation(libs.asm)
    compileOnly(libs.micrometer.core)
    implementation(libs.guava)

    testImplementation(libs.zstd.jni)
    testImplementation(libs.micrometer.core)
    testImplementation(libs.jackson.databind)
    testImplementation(libs.testcontainers.toxiproxy)
    testImplementation(libs.wiremock.standalone)
    testImplementation(libs.mockito.core)
    testImplementation(testFixtures(projects.clickhouseClient))
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

tasks.shadowJar {
    archiveClassifier = "all"
    dependencies {
        exclude(dependency("io.micrometer:.*"))
        exclude(dependency("org.slf4j:.*"))
    }
    val shadeBasePackage = "${project.group}.shaded"
    relocate("net.jpountz", "${shadeBasePackage}.net.jpountz.lz4")
    relocate("org.ow2", "${shadeBasePackage}.org.ow2")
    relocate("org.roaringbitmap", "${shadeBasePackage}.org.roaringbitmap")
    relocate("org.objectweb", "${shadeBasePackage}.org.objectweb")
    manifest {
        attributes["Automatic-Module-Name"] = "com.clickhouse.client.http"
    }
    exclude("**/module-info.class")
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}
