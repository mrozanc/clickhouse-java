import kotlin.io.path.relativeTo
import kotlin.io.path.walk

plugins {
    `java-conventions`
    `all-dependencies-jar`
    antlr
}

dependencies {
    antlr(libs.antlr4)

    api(projects.clientV2)
    implementation(libs.antlr4.runtime)
    implementation(libs.slf4j.api)
    implementation(libs.guava)

    testImplementation(libs.wiremock.standalone)
    testImplementation(libs.commons.lang3)
    testImplementation(testFixtures(projects.clickhouseClient))
}

val antlrBasePath = file("src/main/antlr4").toPath()
val antlrPackage = antlrBasePath.walk().first().parent
    .relativeTo(antlrBasePath).toString()
    .replace(File.separatorChar, '.')

sourceSets.main.get().antlr {
    setSrcDirs(listOf("src/main/antlr4/${antlrPackage.replace('.', '/')}"))
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-package", antlrPackage, "-o", outputDirectory.resolve(antlrPackage.replace('.', '/')).toString())
}

tasks.copyMainSources11 {
    inputs.files(tasks.generateGrammarSource)
}

tasks.jar {
    manifest {
        attributes["Specification-Title"] = "JDBC"
        attributes["Specification-Version"] = "4.2"
    }
}

tasks.sourcesJar {
    dependsOn(tasks.generateGrammarSource)
}
