plugins {
    java
}

//sourceSets.test.get().java.setSrcDirs(listOf("src/test"))

dependencies {
    implementation(project(path = ":clickhouse-jdbc", configuration = "shadow"))
    implementation(libs.slf4j.api)
    implementation(libs.slf4j.simple)
    implementation(libs.jmh.core)
    implementation(libs.testcontainers.clickhouse)

    annotationProcessor(libs.jmh.generator.annprocess)
}

val measureIter: String by properties
val measureTime: String by properties

val runBenchmarks by tasks.registering(JavaExec::class) {
    dependsOn(tasks.jar)
    group = "benchmark"
    description = "Runs the JMH benchmarks."
    mainClass.set("com.clickhouse.benchmark.BenchmarkRunner")
    classpath(sourceSets.main.get().runtimeClasspath, tasks.jar.get().outputs)
    args("-m", measureIter, "-t", measureTime)
}
