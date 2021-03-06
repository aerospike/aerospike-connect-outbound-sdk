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

@file:Suppress("UnstableApiUsage")

import net.researchgate.release.ReleaseExtension
import java.net.URI

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        jcenter()
    }
}

plugins {
    `lifecycle-base`
    jacoco
    `maven-publish`
    signing
    java

    // Gradle version 6.6 compatible with 5.1.x, see https://github.com/freefair/gradle-plugins#compatibility-matrix.
    id("io.freefair.lombok") version "5.1.1"
    id("io.snyk.gradle.plugin.snykplugin")
}

allprojects {
    // Configures the Jacoco tool version to be the same for all projects that have it applied.
    pluginManager.withPlugin("jacoco") {
        // If this project has the plugin applied, configure the tool version.
        jacoco {
            toolVersion = "0.8.5"
        }
    }

    apply {
        plugin(JavaPlugin::class.java)
        plugin("java-library")
        plugin("jacoco")
        plugin("maven-publish")
        plugin("net.researchgate.release")
        plugin("io.freefair.lombok")
        plugin("io.snyk.gradle.plugin.snykplugin")
    }

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }

    group = "com.aerospike"

    // Common dependency versions.
    extra["jupiterVersion"] = "5.8.2"

    dependencies {
        // JSR 305 for annotations
        compileOnly("com.google.code.findbugs:jsr305:3.0.2")

        // Aerospike Java Client
        compileOnly("com.aerospike:aerospike-client:5.1.11")

        // Common test dependencies.
        testImplementation(
            "org.junit.jupiter:junit-jupiter-api:${project.extra["jupiterVersion"]}")
        testImplementation(
            "org.junit.jupiter:junit-jupiter-params:${project.extra["jupiterVersion"]}")
        testRuntimeOnly(
            "org.junit.jupiter:junit-jupiter-engine:${project.extra["jupiterVersion"]}")
    }

    val compileJava: JavaCompile by tasks
    compileJava.sourceCompatibility = "1.8"
    compileJava.targetCompatibility = "1.8"
    compileJava.options.apply {
        compilerArgs.add("-Xlint:all")
        compilerArgs.add("-Werror")
        compilerArgs.add("-Xlint:-processing")
    }

    val compileTestJava: JavaCompile by tasks
    compileTestJava.sourceCompatibility = "1.8"
    compileTestJava.targetCompatibility = "1.8"
    compileTestJava.options.apply {
        compilerArgs.add("-Xlint:all")
        compilerArgs.add("-Werror")
        compilerArgs.add("-Xlint:-processing")
    }

    project.extensions.configure(ReleaseExtension::class) {
        tagTemplate = "\$version"
    }

    tasks.getByName("afterReleaseBuild").dependsOn("publish")

    tasks.javadoc {
        options {
            this as StandardJavadocDocletOptions

            // Fail on Javadoc lint errors.
            addBooleanOption("Xdoclint:all", true)
        }
    }

    /**
     * Common configuration for test tasks.
     */
    fun Test.configureTestTask() {
        val args = mutableListOf(
            "-XX:MaxPermSize=512m",
            "-Xmx4g",
            "-Xms512m",
            "-Djava.security.egd=file:/dev/./urandom",
            "-Dproject.version=${project.version}"
        )


        project.properties.forEach { (property, value) ->
            // Pass along project properties as System properties to the test.
            args += "-D$property=$value"
        }

        // Pass all project versions
        project.parent?.subprojects?.forEach {
            args += "-D${
                it.name.replace("\\W".toRegex(), ".")
            }.version=${it.version}"
        }

        jvmArgs = args
    }

    /**
     * Use Junit for running tests.
     */
    tasks.getByName<Test>("test") {
        useJUnitPlatform {
            // Exclude performance tests in normal runs.
            excludeTags.add("performance")
        }

        configureTestTask()

        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    /**
     * Setup release task properties.
     */
    project.extensions.configure(ReleaseExtension::class) {
        val gitConfig =
            getProperty("git") as net.researchgate.release.GitAdapter.GitConfig
        gitConfig.requireBranch = "main"
    }

    publishing {
        repositories {
            maven {
                val releaseRepo =
                    URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotRepo =
                    URI("https://oss.sonatype.org/content/repositories/snapshots/")
                url = if (!isSnapshotVersion(
                        project.version)) releaseRepo else snapshotRepo
                credentials {
                    username = project.properties["ossrhUsername"] as? String
                    password = project.properties["ossrhPassword"] as? String
                }
            }
        }

        publications {
            create<MavenPublication>("mavenJava") {
                artifactId = project.name
                from(components["java"])
                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
                pom {
                    name.set("Aerospike Connect Outbound SDK")
                    description.set(
                        "Outbound connector SDK for change notification transformers.")
                    url.set(
                        "https://github.com/aerospike/aerospike-connect-outbound-sdk")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set(
                                "https://www.apache.org/licenses/LICENSE-2.0" +
                                        ".txt")
                        }
                    }
                    scm {
                        connection.set(
                            "scm:git@github.com:aerospike/aerospike-connect-outbound-sdk.git")
                        developerConnection.set(
                            "scm:git@github.com:aerospike/aerospike-connect-outbound-sdk.git")
                        url.set(
                            "https://github.com/aerospike/aerospike-connect-outbound-sdk")
                    }
                    developers {
                        developer {
                            name.set("Aerospike")
                            email.set("developers@aerospike.com")
                            organization.set("Aerospike")
                            url.set("https://www.aerospike.com/")
                        }
                    }
                }
            }
        }

        tasks.withType<PublishToMavenRepository>().configureEach {
            onlyIf {
                // Upload if snapshot version.
                // If a proper release version upload only when release task is
                // present. This prevents re-releasing re-builds of released
                // version.
                isSnapshotVersion(project.version) || hasReleaseTask()
            }
        }
    }

    signing {
        sign(publishing.publications.getByName("mavenJava"))
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    // Setup vulnerability scanning.
    val snykTokens: String by project
    val snykToken = snykTokens.split(",").map { it.trim() }.random()

    tasks.create<Exec>("setup-snyk") {
        commandLine("${project.rootDir}/snyk", "auth", snykToken)
    }
    tasks.getByName("snyk-check-binary").finalizedBy("setup-snyk")

    /**
     * Vulnerability scanning with Snyk.
     */
    configure<io.snyk.gradle.plugin.SnykExtension> {
        setApi(snykToken)
        setSeverity("high")
        setAutoDownload(true)
        setArguments("--sub-project=" + project.name)
    }
}

/**
 * Check if current project version is a snapshot version.
 */
fun isSnapshotVersion(version: Any): Boolean {
    return version.toString().endsWith("-SNAPSHOT")
}

/**
 * Check if we are running a release task.
 */
fun hasReleaseTask(): Boolean {
    val releaseTaskName = "afterReleaseBuild"
    var hasRelease = false
    gradle.taskGraph.allTasks.forEach {
        if (it.name == releaseTaskName) {
            hasRelease = true
            return@forEach
        }
    }
    return hasRelease
}
