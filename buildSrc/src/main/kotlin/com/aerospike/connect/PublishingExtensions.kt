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

package com.aerospike.connect

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import java.net.URI

/**
 * Setup publishing tasks.
 */
fun Project.setupPublishingTasks() {
    val publishing =
        (project.extensions["publishing"] as PublishingExtension)

    publishing.repositories {
        maven {
            val releaseRepo =
                URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotRepo =
                URI("https://oss.sonatype.org/content/repositories/snapshots/")
            url = if (!isSnapshotVersion()) releaseRepo else snapshotRepo
            credentials {
                username = project.properties["ossrhUsername"] as? String
                password = project.properties["ossrhPassword"] as? String
            }
        }
    }

    publishing.publications {
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
                            "https://www.apache.org/licenses/LICENSE-2.0.txt")
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
            isSnapshotVersion() || hasReleaseTask()
        }
    }

    val signing = (project.extensions["signing"] as SigningExtension)
    signing.sign(publishing.publications.getByName("mavenJava"))
}
