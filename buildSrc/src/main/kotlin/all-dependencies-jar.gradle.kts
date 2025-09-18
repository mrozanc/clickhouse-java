import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java-conventions")
    id("com.gradleup.shadow")
}

val allDependenciesJar by tasks.registering(ShadowJar::class) {
    group = "build"
    description = "Assembles a jar archive containing all runtime dependencies"
    archiveClassifier = "all-dependencies"
    configurations = listOf(project.configurations.compileClasspath.get())
    from(sourceSets.main.get().output)
    exclude("**/module-info.class")
    manifest {
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = project.version.toString()
        attributes["Implementation-Vendor-Id"] = project.group.toString()
    }
}

val allDependenciesJarConfiguration = configurations.create("allDependenciesJar").apply {
    attributes {
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
    }
}

val allDependenciesJarArtifact = artifacts.add(allDependenciesJarConfiguration.name, allDependenciesJar)
val javaComponent = components["java"] as AdhocComponentWithVariants
javaComponent.addVariantsFromConfiguration(allDependenciesJarConfiguration) {
    mapToMavenScope("runtime")
    mapToOptional()
}

tasks.assemble {
    dependsOn(allDependenciesJar)
}
