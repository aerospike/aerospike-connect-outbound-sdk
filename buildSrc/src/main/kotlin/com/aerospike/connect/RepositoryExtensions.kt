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
import java.util.Base64

/**
 * Resolve QE/DEV SDK artifacts from connect Maven DEV.
 *
 * Credentials (first match wins):
 * - Gradle properties `connectSDKDevRepoUser` / `connectSDKDevRepoPassword`
 * - `CONNECT_SDK_DEV_REPO_USER` / `CONNECT_SDK_DEV_REPO_PASSWORD`
 * - JFrog CLI / GitHub OIDC: `JF_USER` / `JF_ACCESS_TOKEN`
 *
 * JFrog CLI OIDC often exports only `JF_ACCESS_TOKEN`. A token without a
 * username still authenticates; the JWT `sub` is used when no user is set.
 */
fun Project.addConnectSdkDevMavenRepository() {
    val password = firstNonBlank(
        findProperty("connectSDKDevRepoPassword") as String?,
        System.getenv("CONNECT_SDK_DEV_REPO_PASSWORD"),
        System.getenv("JF_ACCESS_TOKEN")
    )
    val user = firstNonBlank(
        findProperty("connectSDKDevRepoUser") as String?,
        System.getenv("CONNECT_SDK_DEV_REPO_USER"),
        System.getenv("JF_USER"),
        jwtSubject(password)
    )

    repositories {
        maven {
            name = "connectMavenDev"
            url = uri(
                "https://artifact.aerospike.io/artifactory/connect-maven-dev-local/"
            )
            if (password != null) {
                credentials {
                    username = user ?: "oidc"
                    this.password = password
                }
            }
        }
    }
}

private fun firstNonBlank(vararg values: String?): String? =
    values.firstOrNull { !it.isNullOrBlank() }

private fun jwtSubject(token: String?): String? {
    if (token.isNullOrBlank()) {
        return null
    }
    val parts = token.split('.')
    if (parts.size < 2) {
        return null
    }
    return try {
        val padded = parts[1] + "=".repeat((4 - parts[1].length % 4) % 4)
        val json = String(Base64.getUrlDecoder().decode(padded))
        Regex("\"sub\"\\s*:\\s*\"([^\"]+)\"")
            .find(json)
            ?.groupValues
            ?.get(1)
    } catch (_: Exception) {
        null
    }
}
