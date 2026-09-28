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

package com.aerospike.connect

import org.gradle.api.Project
import org.gradle.kotlin.dsl.repositories

/**
 * Resolve QE/DEV SDK artifacts from connect Maven DEV.
 *
 * Credentials (first match wins):
 * - Gradle properties `connectSDKDevRepoUser` / `connectSDKDevRepoPassword`
 * - `CONNECT_SDK_DEV_REPO_USER` / `CONNECT_SDK_DEV_REPO_PASSWORD`
 * - JFrog CLI OIDC: `JF_USER` / `JF_ACCESS_TOKEN`
 */
fun Project.addConnectSdkDevMavenRepository() {
    val user = firstNonBlank(
        findProperty("connectSDKDevRepoUser") as String?,
        System.getenv("CONNECT_SDK_DEV_REPO_USER"),
        System.getenv("JF_USER")
    )
    val password = firstNonBlank(
        findProperty("connectSDKDevRepoPassword") as String?,
        System.getenv("CONNECT_SDK_DEV_REPO_PASSWORD"),
        System.getenv("JF_ACCESS_TOKEN")
    )

    repositories {
        maven {
            name = "connectMavenDev"
            url = uri(
                "https://aerospike.jfrog.io/artifactory/connect-maven-dev-local/"
            )
            if (user != null && password != null) {
                credentials {
                    username = user
                    this.password = password
                }
            }
        }
    }
}

private fun firstNonBlank(vararg values: String?): String? =
    values.firstOrNull { !it.isNullOrBlank() }
