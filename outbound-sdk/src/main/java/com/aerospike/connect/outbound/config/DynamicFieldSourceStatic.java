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

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * Use Aerospike record's bin value to extract a value.
 */
@Builder
@Jacksonized
@Value
public class DynamicFieldSourceStatic implements DynamicFieldSource {
    /**
     * A static value to be used as a value.
     *
     * @param value A static value to be used.
     * @return a static value to be used.
     */
    @NonNull
    String value;

    @Override
    public void validate() throws Exception {
        if (value.trim().isEmpty()) {
            throw new Exception("value cannot be blank");
        }
    }

    @Override
    public DynamicFieldSourceFailureStrategy
    getDynamicFieldSourceFailureStrategy() throws Exception {
        throw new Exception("value should always be available");
    }
}
