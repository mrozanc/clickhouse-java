plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        api(libs.clickhouse.commons.compress)
        api(libs.clickhouse.javacc)
        api(libs.clickhouse.roaringbitmap)
        api(libs.asm)
        api(libs.commons.compress)
        api(libs.commons.lang3)
        api(libs.lz4.java)
        api(libs.lz4.pure.java)
        api(libs.brotli4j)
        api(libs.brotli4j.native.linux.x64)
        api(libs.brotli4j.native.osx.x64)
        api(libs.brotli4j.native.windows.x64)
        api(libs.caffeine)
        api(libs.zstd.jni)
        api(libs.dnsjava)
        api(libs.gson)
        api(libs.tomcat.annotations.api)
        api(libs.brotli.dec)
        api(libs.jctools.core)
        api(libs.roaringbitmap)
        api(libs.slf4j.api)
        api(libs.slf4j.simple)
        api(libs.xz)
        api(libs.snappy.java)
        api(libs.testcontainers)
        api(libs.testcontainers.toxiproxy)
        api(libs.testng)
        api(libs.mariadb.java.client)
        api(libs.mysql.connector)
        api(libs.postgresql)
    }
}
