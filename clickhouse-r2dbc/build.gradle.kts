import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.ApacheLicenseResourceTransformer
import com.github.jengelman.gradle.plugins.shadow.transformers.ApacheNoticeResourceTransformer
import org.gradle.kotlin.dsl.withType
import java.util.jar.Attributes
import kotlin.io.path.relativeTo
import kotlin.io.path.walk

plugins {
    `java-conventions`
    id("com.gradleup.shadow")
}

description = "R2DBC driver for ClickHouse"

java {
    registerFeature("r2dbc0") {
        val r2dbc0SourceSet = sourceSets.create("r2dbc0").apply {
            java {
                val r2dbc0Files = srcDirs.asSequence()
                    .map { it.toPath() }
                    .flatMap { d -> d.walk().map { it.relativeTo(d).toString() } }
                    .toSet()
                srcDir(sourceSets.main.get().java.srcDirs)
                exclude { f ->
                    val isR2dbc0 = r2dbc0Files.contains(f.path)
                    val isMainRoot =
                        sourceSets.main.get().java.srcDirs.any { mainSrc -> f.file.absolutePath.startsWith(mainSrc.absolutePath) }
                    isR2dbc0 && isMainRoot
                }
            }
        }
        usingSourceSet(r2dbc0SourceSet)
        withJavadocJar()
        withSourcesJar()
    }
}

configurations {
    "r2dbc0CompileClasspath" {
        extendsFrom(compileClasspath.get())
    }
    "r2dbc0RuntimeClasspath" {
        extendsFrom(runtimeClasspath.get())
    }
}

dependencies {
    constraints {
        "r2dbc0Api"(libs.r2dbc0.spi)
    }
    "r2dbc0Api"(sourceSets.main.get().output)
    api(projects.clickhouseClient)
    api(libs.reactor.core)
    api(libs.r2dbc.spi)
    api(projects.clickhouseHttpClient)
    compileOnly(libs.clickhouse.roaringbitmap) { isTransitive = false }
    implementation(libs.lz4.java)

    testImplementation(testFixtures(projects.clickhouseClient))
    testImplementation(projects.clickhouseJdbc)
    testImplementation(libs.hikaricp) {
        exclude("org.slf4j", "slf4j-api")
    }
    testImplementation(libs.r2dbc.spi.test)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.assemble {
    dependsOn("r2dbc0Jar")
}

tasks.test {
    useJUnitPlatform()
}

val baseRelocationPackage = "${project.group}.client.internal"

tasks.withType<ShadowJar>().configureEach {
    relocate("net.jpountz", "${baseRelocationPackage}.jpountz")
    transform<ApacheLicenseResourceTransformer>()
    transform<ApacheNoticeResourceTransformer>()
    mergeServiceFiles()
    manifest {
        attributes["Automatic-Module-Name"] = "${project.group}.r2dbc"
        attributes[Attributes.Name.SPECIFICATION_TITLE.toString()] = "R2DBC"
        attributes[Attributes.Name.SPECIFICATION_VERSION.toString()] = "1.0"
    }
    dependencies {
        exclude(dependency("io.projectreactor:reactor-core"))
        exclude(dependency("io.r2dbc:r2dbc-spi"))
        exclude(dependency("org.reactivestreams:reactive-streams"))
    }
    exclude(
        "google/**",
        "mozilla/**",
        "org/checkerframework/**",
        "org/codehaus/**",
        "**/module-info.class",
        "META-INF/DEPENDENCIES",
        "META-INF/MANIFEST.MF",
        "META-INF/maven/**",
        "META-INF/native-image/**",
        "META-INF/*.xml",
    )
}

tasks.shadowJar {
    archiveClassifier = "all"
    relocate("com.google", "${baseRelocationPackage}.google")
    relocate("io.opencensus", "${baseRelocationPackage}.opencensus")
    relocate("io.perfmark", "${baseRelocationPackage}.perfmark")
    relocate("okio", "${baseRelocationPackage}.okio")
    relocate("org.apache", "${baseRelocationPackage}.apache")
}

val httpJar by tasks.registering(ShadowJar::class) {
    archiveClassifier = "http"
    configurations = listOf(project.configurations.runtimeClasspath.get())
    from(sourceSets.main.get().output)
    dependencies {
        exclude(dependency("${project.group}:org.roaringbitmap"))
        exclude(dependency("com.google.code.gson:gson"))
    }
    exclude(
        "**/darwin/**",
        "**/linux/**<",
        "**/win32/**",
        "META-INF/native/**",
    )
}

val httpJarConfiguration = configurations.create("httpJar").apply {
    attributes {
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
    }
}

val allDependenciesJarArtifact = artifacts.add(httpJarConfiguration.name, httpJar)
val javaComponent = components["java"] as AdhocComponentWithVariants
javaComponent.addVariantsFromConfiguration(httpJarConfiguration) {
    mapToMavenScope("runtime")
    mapToOptional()
}

tasks.assemble {
    dependsOn(httpJar)
}


