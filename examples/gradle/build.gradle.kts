/*
 *
 *  Copyright 2012-2025 Aerospike, Inc.
 *
 *  Portions may be licensed to Aerospike, Inc. under one or more contributor
 *  license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("io.freefair.gradle:lombok-plugin:8.14")
    }
}

repositories {
    mavenCentral()

    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
    maven {
        val connectSnapshotsRepo: String by project
        val connectSnapshotsRepoUser: String by project
        val connectSnapshotsRepoPassword: String by project
        url = uri(connectSnapshotsRepo)
        credentials {
            username = connectSnapshotsRepoUser
            password = connectSnapshotsRepoPassword
        }
    }
}

configurations.all {
    // Check for updates every build
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

dependencies {
    // Aerospike outbound SDK.
    compileOnly("com.aerospike:aerospike-connect-outbound-sdk:2.2.2-SNAPSHOT")
    compileOnly(
        "com.aerospike:aerospike-connect-elasticsearch-outbound-sdk:2.1.4-SNAPSHOT"
    )
    compileOnly("co.elastic.clients:elasticsearch-java:8.18.2")

    // Logging.
    compileOnly("org.slf4j:slf4j-api:2.0.17")

    // Lombok's annotations.
    compileOnly("org.projectlombok:lombok:1.18.38")

    // Jakarta annotations.
    compileOnly("jakarta.annotation:jakarta.annotation-api:3.0.0")
    compileOnly("jakarta.inject:jakarta.inject-api:2.0.1")
}

// Plugin should be compiled with the same/compatible Java version running
// the outbound connector.
val compileJava: JavaCompile by tasks
compileJava.sourceCompatibility = JavaVersion.VERSION_11.majorVersion
compileJava.targetCompatibility = JavaVersion.VERSION_11.majorVersion
compileJava.options.apply {
    compilerArgs.add("-Xlint:all")
    compilerArgs.add("-Werror")
}

plugins {
    `java-library`
    id("com.gradleup.shadow") version "9.1.0"
}

apply {
    plugin("com.gradleup.shadow") // Shade dependencies.
    plugin("java")
    plugin("io.freefair.lombok")
}

// Shade dependencies to avoid any class dependency conflicts.
tasks.shadowJar {
    relocate("com.fasterxml", "com.aerospike.connect.shaded.com.fasterxml")
}
