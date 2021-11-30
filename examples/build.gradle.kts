/*
 *
 *  Copyright 2012-2021 Aerospike, Inc.
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
}

dependencies {
    // JSON formatting in some examples.
    api("com.fasterxml.jackson.core:jackson-databind:2.6.6")

    // Aerospike client.
    compileOnly("com.aerospike:aerospike-client:5.1.7")

    // Aerospike outbound SDK.
    compileOnly("com.aerospike:aerospike-connect-outbound-sdk:1.0.0")

    // Lombok annotations.
    compileOnly("org.projectlombok:lombok:1.18.22")

    // Javax inject annotations.
    compileOnly("javax.inject:javax.inject:1")
}

// Use Java 8.
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
