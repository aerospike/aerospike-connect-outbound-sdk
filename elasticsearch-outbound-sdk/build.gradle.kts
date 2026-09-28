/*
 *
 *  Copyright 2012-2026 Aerospike, Inc.
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

import groovy.util.Node
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

val outboundSdkVersion = "3.0.2-1"

dependencies {
    // Published outbound SDK (3.0.2-1 is in connect Maven DEV).
    api("com.aerospike:aerospike-connect-outbound-sdk:$outboundSdkVersion")

    // Elasticsearch client
    api("co.elastic.clients:elasticsearch-java:9.4.3") {
        // Exclude unused vulnerable/incpmatible transitive dependencies
        exclude("io.opentelemetry", "opentelemetry-api")
        exclude("org.apache.httpcomponents.client5", "httpclient5")
        exclude("org.apache.httpcomponents.core5", "httpcore5-h2")
        exclude("org.eclipse.parsson", "parsson")
        exclude("tools.jackson.core")
        exclude("tools.jackson")
    }
}

// CI compiles against in-repo sources so GitHub Actions does not need JFrog.
// Release sets USE_PUBLISHED_OUTBOUND_SDK=true to resolve the DEV GAV.
val usePublishedOutboundSdk =
    providers.environmentVariable("USE_PUBLISHED_OUTBOUND_SDK")
        .orElse(providers.gradleProperty("usePublishedOutboundSdk"))
        .map { it.equals("true", ignoreCase = true) }
        .orElse(false)

if (!usePublishedOutboundSdk.get()) {
    configurations.configureEach {
        resolutionStrategy.dependencySubstitution {
            substitute(module("com.aerospike:aerospike-connect-outbound-sdk"))
                .using(project(":aerospike-connect-outbound-sdk"))
        }
    }
}

afterEvaluate {
    extensions.getByType<PublishingExtension>().publications
        .named<MavenPublication>("mavenJava") {
            pom.withXml {
                asNode().children().filterIsInstance<Node>().forEach { node ->
                    if (!node.name().toString().endsWith("dependencies")) {
                        return@forEach
                    }
                    node.children().filterIsInstance<Node>().forEach { dep ->
                        val children = dep.children().filterIsInstance<Node>()
                        val artifactId = children
                            .firstOrNull { it.name().toString().endsWith("artifactId") }
                            ?.text()
                        if (artifactId == "aerospike-connect-outbound-sdk") {
                            children.first { it.name().toString().endsWith("version") }
                                .setValue(outboundSdkVersion)
                        }
                    }
                }
            }
        }
}
