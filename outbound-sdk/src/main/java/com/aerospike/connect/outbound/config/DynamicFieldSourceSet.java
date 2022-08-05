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
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import static com.aerospike.connect.outbound.config.DynamicFieldSourceFailureStrategy.USE_DIGEST;

/**
 * The Aerospike record's set should be used as a value.
 */
@Builder
@Jacksonized
@Value
public class DynamicFieldSourceSet implements DynamicFieldSource {
    /**
     * Strategy to be used when the value cannot be computed using the specified
     * source.
     *
     * @param dynamicFieldSourceFailureStrategy The
     * {@link DynamicFieldSourceFailureStrategy}.
     * @return the {@link DynamicFieldSourceFailureStrategy}.
     */
    @Builder.Default
    @JsonProperty("failure-strategy")
    DynamicFieldSourceFailureStrategy dynamicFieldSourceFailureStrategy =
            USE_DIGEST;
}
