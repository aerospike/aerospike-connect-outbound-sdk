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

package com.aerospike.connect.outbound;

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The Aerospike Bin related data.
 */
@EqualsAndHashCode
@Getter
@ToString
public class BinData {
    /**
     * The bin value.
     */
    @Nullable
    private final Object value;

    /**
     * The bin's last-updated-time.
     */
    @Nullable
    private final Long lastUpdateTimeMs;

    public BinData(@Nullable Object value, @Nullable Long lastUpdateTimeMs) {
        this.value = value;
        this.lastUpdateTimeMs = lastUpdateTimeMs;
    }
}
