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

repositories {
    mavenCentral()

    maven {
        // TODO - REMOVE ON RELEASE.
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    // JSON formatting in some examples.
    api("com.fasterxml.jackson.core:jackson-databind:2.6.6")

    // Aerospike client.
    compileOnly("com.aerospike:aerospike-client:5.1.8")

    // Aerospike outbound SDK.
    // TODO: update to 1.0.0 on release.
    compileOnly("com.aerospike:aerospike-connect-outbound-sdk:0.9.0-SNAPSHOT")

    // Logging.
    compileOnly("org.slf4j:slf4j-api:1.7.26")

    // Lombok annotations.
    compileOnly("org.projectlombok:lombok:1.18.22")

    // Javax inject annotations.
    compileOnly("javax.inject:javax.inject:1")

    // Google Pub/Sub ByteString.
    compileOnly("com.google.protobuf:protobuf-java:3.13.0")
}

// Plugin should be compiled with the same/compatible Java version running
// the outbound connector.
val compileJava: JavaCompile by tasks
compileJava.targetCompatibility = "1.8"
compileJava.options.apply {
    compilerArgs.add("-Xlint:all")
    compilerArgs.add("-Werror")
}

val compileTestJava: JavaCompile by tasks
compileTestJava.targetCompatibility = "1.8"
compileTestJava.options.apply {
    compilerArgs.add("-Xlint:all")
    compilerArgs.add("-Werror")
}

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

apply {
    plugin("com.github.johnrengelman.shadow") // Shade dependencies.
    plugin("java")
}

// Shade dependencies to avoid any class dependency conflicts.
tasks.shadowJar {
    relocate("com.fasterxml", "com.aerospike.connect.shaded.com.fasterxml")
}
