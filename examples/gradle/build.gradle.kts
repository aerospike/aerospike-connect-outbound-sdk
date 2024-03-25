/*
 *
 *  Copyright 2012-2022 Aerospike, Inc.
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
        classpath("io.freefair.gradle:lombok-plugin:8.6")
    }
}

repositories {
    mavenCentral()

    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

configurations.all {
    // Check for updates every build
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

dependencies {
    // Aerospike client.
    compileOnly("com.aerospike:aerospike-client-jdk8:8.1.0")

    // JSON formatting in some examples.
    api("com.fasterxml.jackson.core:jackson-databind:2.15.3")

    // Aerospike outbound SDK.
    compileOnly("com.aerospike:aerospike-connect-outbound-sdk:2.2.0")
    compileOnly(
        "com.aerospike:aerospike-connect-elasticsearch-outbound-sdk:2.1.2"
    )
    compileOnly("co.elastic.clients:elasticsearch-java:8.12.2")

    // Logging.
    compileOnly("org.slf4j:slf4j-api:2.0.12")

    // Lombok's annotations.
    compileOnly("org.projectlombok:lombok:1.18.32")

    // Javax inject annotations.
    compileOnly("javax.inject:javax.inject:1")
}

// Plugin should be compiled with the same/compatible Java version running
// the outbound connector.
val compileJava: JavaCompile by tasks
compileJava.sourceCompatibility = "1.8"
compileJava.targetCompatibility = "1.8"
compileJava.options.apply {
    compilerArgs.add("-Xlint:all")
    compilerArgs.add("-Werror")
    // Suppress warning: [options] source value 8 is obsolete and will be
    // removed in a future release.
    compilerArgs.add("-Xlint:-options")
}

val compileTestJava: JavaCompile by tasks
compileTestJava.sourceCompatibility = "1.8"
compileTestJava.targetCompatibility = "1.8"
compileTestJava.options.apply {
    compilerArgs.add("-Xlint:all")
    compilerArgs.add("-Werror")
    // Suppress warning: [options] source value 8 is obsolete and will be
    // removed in a future release.
    compilerArgs.add("-Xlint:-options")
}

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

apply {
    plugin("com.github.johnrengelman.shadow") // Shade dependencies.
    plugin("java")
    plugin("io.freefair.lombok")
}

// Shade dependencies to avoid any class dependency conflicts.
tasks.shadowJar {
    relocate("com.fasterxml", "com.aerospike.connect.shaded.com.fasterxml")
}
