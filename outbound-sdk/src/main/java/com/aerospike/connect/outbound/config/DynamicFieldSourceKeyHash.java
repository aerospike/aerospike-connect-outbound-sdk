/*
 *
 *  Copyright 2012-2023 Aerospike, Inc.
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

import lombok.EqualsAndHashCode;

/**
 * Hash the Change Notification Record keys in a batch into a fixed length byte
 * array.
 */
@EqualsAndHashCode
public class DynamicFieldSourceKeyHash implements DynamicFieldSource {
    @Override
    public DynamicFieldSourceFailureStrategy getDynamicFieldSourceFailureStrategy()
            throws Exception {
        throw new Exception("key-hash is always possible");
    }

    @Override
    public boolean isAllowedForBatchRecordKey() {
        return true;
    }

    @Override
    public boolean isAllowedForSingleRecordKey() {
        return false;
    }
}
