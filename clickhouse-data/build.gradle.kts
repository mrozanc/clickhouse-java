plugins {
    `java-conventions`
}

java {
    registerFeature("compression") {
        usingSourceSet(sourceSets.main.get())
    }
    registerFeature("cache") {
        usingSourceSet(sourceSets.main.get())
    }
    registerFeature("json") {
        usingSourceSet(sourceSets.main.get())
    }
    registerFeature("adaptiveQueue") {
        usingSourceSet(sourceSets.main.get())
    }
    registerFeature("asm") {
        usingSourceSet(sourceSets.main.get())
    }
    registerFeature("loggingApi") {
        usingSourceSet(sourceSets.main.get())
    }
}

dependencies {
    compileOnly(libs.clickhouse.roaringbitmap)
    implementation(libs.commons.compress)
    "compressionImplementation"(libs.xz)
    "compressionImplementation"(libs.snappy.java)
    "compressionImplementation"(libs.brotli4j)
    "compressionImplementation"(libs.brotli.dec)
    "compressionImplementation"(libs.zstd.jni)
    "compressionImplementation"(libs.lz4.java)
    "cacheImplementation"(libs.caffeine)
    "jsonImplementation"(libs.gson)
    "adaptiveQueueImplementation"(libs.jctools.core)
    "asmImplementation"(libs.asm)
    "loggingApiImplementation"(libs.slf4j.api)

    java11CompileOnly(libs.xz)
    java11CompileOnly(libs.snappy.java)
    java11CompileOnly(libs.brotli4j)
    java11CompileOnly(libs.brotli.dec)
    java11CompileOnly(libs.zstd.jni)
    java11CompileOnly(libs.lz4.java)
    java11CompileOnly(libs.caffeine)
    java11CompileOnly(libs.gson)
    java11CompileOnly(libs.jctools.core)
    java11CompileOnly(libs.asm)
    java11CompileOnly(libs.slf4j.api)

    testImplementation(libs.clickhouse.roaringbitmap)
    testImplementation(libs.slf4j.simple)
    testImplementation(libs.testcontainers)
    testImplementation(libs.testng)
}

tasks.named<JavaCompile>("compileJava11Java") {
    options.compilerArgs.add("--add-reads")
    options.compilerArgs.add("com.clickhouse.data=ALL-UNNAMED")
}

tasks {
    test {
        environment("CHC_STR", "env_str")
        environment("CHC_INT", "416")
        environment("CHC_BOOL", "false")
    }
}

