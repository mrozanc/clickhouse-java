import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.ApacheLicenseResourceTransformer
import com.github.jengelman.gradle.plugins.shadow.transformers.ApacheNoticeResourceTransformer
import java.util.jar.Attributes

plugins {
    `java-conventions`
    `all-dependencies-jar`
    alias(libs.plugins.javacc)
}

dependencies {
    javacc(projects.javaccAdapter)

    api(projects.clickhouseClient)
    api(projects.clickhouseData)
    api(projects.clickhouseHttpClient)
    api(projects.jdbcV2)
    implementation(libs.commons.compress)
    compileOnly(libs.roaringbitmap) { isTransitive = false }
    compileOnly(libs.clickhouse.commons.compress) { isTransitive = false }
    implementation(libs.lz4.java)
    compileOnly(libs.zstd.jni)
    implementation(libs.slf4j.api)

    testImplementation(libs.roaringbitmap) { isTransitive = false }
    testImplementation(libs.clickhouse.commons.compress) { isTransitive = false }
    testImplementation(libs.zstd.jni)
    testImplementation(testFixtures(projects.clickhouseClient))
    testImplementation(libs.mysql.connector) { isTransitive = false }
}

tasks.compileJavacc {
    dependsOn(":javacc-adapter:jar")
}

val baseRelocationPackage = "${project.group}.client.internal"
fun ShadowJar.relocate(vararg pkgs: String) =
    pkgs.forEach { pkg -> relocate(pkg, "${baseRelocationPackage}.$pkg") }

tasks.withType<ShadowJar>().configureEach {
    dependencies {
        exclude(dependency("io.micrometer:.*"))
    }
    relocate(
        "net.jpountz",
        "org.roaringbitmap",
        "org.objectweb",
        "com.google",
        "org.apache",
        "org.antlr"
    )
    transform<ApacheLicenseResourceTransformer>()
    transform<ApacheNoticeResourceTransformer>()
    mergeServiceFiles()
    manifest {
        attributes["Automatic-Module-Name"] = "${project.group}.jdbc"
        attributes[Attributes.Name.MAIN_CLASS.toString()] = "com.clickhouse.jdbc.Main"
        attributes[Attributes.Name.SPECIFICATION_TITLE.toString()] = "JDBC"
        attributes[Attributes.Name.SPECIFICATION_VERSION.toString()] = "4.2"
    }
    exclude(
        "google/**",
        "org/checkerframework/**",
        "org/codehaus/**",
        "**/module-info.class",
    )
}

tasks.shadowJar {
    archiveClassifier = "all"
    dependencies {
        exclude(dependency("org.slf4j:.*"))
    }
}
