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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

/**
 * Generates static [null]/no value for the field.
 */
@EqualsAndHashCode
public class DynamicFieldSourceNone implements DynamicFieldSource {
    @JsonIgnore
    @Override
    public DynamicFieldSourceFailureStrategy
    getDynamicFieldSourceFailureStrategy() throws Exception {
        throw new Exception(
                "destination system will auto-generate or use null");
    }

    @Override
    public boolean isAllowedForBatchRecordKey() {
        return true;
    }
}
