plugins {
    `java-conventions`
    id("com.gradleup.shadow")
}

java {
    registerFeature("compression") {
        usingSourceSet(sourceSets.main.get())
    }
}

dependencies {
    api(projects.clickhouseClient)
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
    testImplementation(testFixtures(projects.clickhouseClient))
}

tasks.shadowJar {
    archiveClassifier = "shaded"
    configurations = listOf(project.configurations.compileClasspath.get())
    dependencies {
        include(project(projects.clickhouseData))
        include(project(projects.clickhouseClient))
        include(dependency(libs.lz4.pure.java.get().module.toString()))
    }
    relocate("net.jpountz", "${project.group}.client.internal.jpountz")
    manifest {
        attributes["Automatic-Module-Name"] = "com.clickhouse.client.http"
    }
    exclude("**/module-info.class")
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}
