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

package com.aerospike.connect.outbound.config;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * How to handle failures in computing a value.
 */
public enum DynamicFieldSourceFailureStrategy {
    /**
     * Use Aerospike record's Base64 encoded [com.aerospike.client.Key.digest]
     * if we cannot compute the value. This is a default as digest is always
     * available.
     */
    USE_DIGEST,

    /**
     * Fail the record with temporary error so that XDR will retry.
     */
    FAIL,

    /**
     * Send permanent error to XDR so that XDR will not retry.
     */
    IGNORE;

    @JsonCreator
    public static DynamicFieldSourceFailureStrategy fromString(String strategy)
            throws Exception {
        DynamicFieldSourceFailureStrategy dynamicFieldSourceFailureStrategy;
        switch (strategy.replace("-", "_").toLowerCase()) {
            case "use_digest":
                dynamicFieldSourceFailureStrategy = USE_DIGEST;
                break;
            case "fail":
                dynamicFieldSourceFailureStrategy = FAIL;
                break;
            case "ignore":
                dynamicFieldSourceFailureStrategy = IGNORE;
                break;
            default:
                throw new Exception("unknown failure-strategy " + strategy);
        }
        return dynamicFieldSourceFailureStrategy;
    }
}
