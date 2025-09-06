import kotlin.io.path.PathWalkOption
import kotlin.io.path.relativeTo
import kotlin.io.path.walk

plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
    withSourcesJar()
    consistentResolution {
        useCompileClasspathVersions()
    }
}

fun registerJavaTarget(javaVersion: Int) {
    val sourceDir = file("src/main/java$javaVersion")
    if (sourceDir.isDirectory) {
        val copySourcesDestination = project.layout.buildDirectory.dir("tmp/java${javaVersion}Sources")
        val sourceRelativePaths = sourceDir.toPath().walk().map {
            it.relativeTo(sourceDir.toPath()).toString().replace('\\', '/')
        }.toList()
        val copyMainSourcesTask = tasks.register<Sync>("copyMainSources$javaVersion") {
            from(sourceSets.main.get().java.srcDirs) {
                exclude(sourceRelativePaths)
            }
            from(sourceDir)
            into(copySourcesDestination)
        }

        val javaSourceSet = sourceSets.create("java$javaVersion") {
            java {
                srcDir(sourceDir)
            }
            compileClasspath += sourceSets.main.get().compileClasspath
        }
        tasks.named<JavaCompile>(javaSourceSet.compileJavaTaskName) {
            javaCompiler = javaToolchains.compilerFor {
                languageVersion.set(JavaLanguageVersion.of(javaVersion))
            }
            setSource(copyMainSourcesTask.get().outputs)
        }
        tasks.named<Jar>("jar").configure {
            val classPatterns = sourceDir.toPath().walk().flatMap {
                val basePattern = it.relativeTo(sourceDir.toPath()).toString().replace('\\', '/').removeSuffix(".java")
                sequenceOf("$basePattern.class", $$"$$basePattern$*.class")
            }.toList()
            into("META-INF/versions/$javaVersion") {
                from(javaSourceSet.output) {
                    include(classPatterns)
                }
            }
            if (!manifest.attributes.contains("Multi-Release")) {
                manifest.attributes("Multi-Release" to true)
            }
        }
    }
}

registerJavaTarget(11)
registerJavaTarget(17)

tasks {
    compileJava {
        options.compilerArgs.add("-source")
        options.compilerArgs.add("1.8")
        options.compilerArgs.add("-target")
        options.compilerArgs.add("1.8")
        //sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        //targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }
    test {
        useTestNG()
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}
