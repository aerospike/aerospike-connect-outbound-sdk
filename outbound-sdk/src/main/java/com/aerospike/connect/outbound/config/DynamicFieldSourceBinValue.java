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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.aerospike.connect.outbound.config.DynamicFieldSourceFailureStrategy.USE_DIGEST;

/**
 * Use Aerospike record's bin value to extract a value.
 */
@AllArgsConstructor
@Getter
public class DynamicFieldSourceBinValue implements DynamicFieldSource {
    @JsonProperty("failure-strategy")
    private final DynamicFieldSourceFailureStrategy
            dynamicFieldSourceFailureStrategy = USE_DIGEST;

    /**
     * Aerospike record's bin whose value should be used as a value.
     *
     * @return an Aerospike bin-name to extract a value from.
     */
    @JsonProperty("bin-name")
    private final String binName;

    @Override
    public void validate() throws Exception {
        if (binName.trim().isEmpty()) {
            throw new Exception("bin-name cannot be blank");
        }
    }

    @Override
    public boolean equals(Object other) {
        return other != null && getClass().equals(other.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
