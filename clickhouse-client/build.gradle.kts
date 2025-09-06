plugins {
    `java-conventions`
    `java-test-fixtures`
}

java {
    registerFeature("dns") {
        usingSourceSet(sourceSets.main.get())
    }
}

dependencies {
    api(project(":clickhouse-data"))
    compileOnly(libs.clickhouse.roaringbitmap) {
        isTransitive = false
    }
    "dnsImplementation"(libs.dnsjava) {
        exclude("org.slf4j", "slf4j-api")
    }

    java11CompileOnly(libs.dnsjava) {
        exclude("org.slf4j", "slf4j-api")
    }

    testFixturesApi(libs.slf4j.simple)
    testFixturesApi(libs.slf4j.api)
    testFixturesApi(libs.testcontainers)
    testFixturesApi(libs.testng)
}

tasks {
    test {
        environment("CHC_STR", "env_str")
        environment("CHC_INT", "416")
        environment("CHC_BOOL", "false")
    }
}
