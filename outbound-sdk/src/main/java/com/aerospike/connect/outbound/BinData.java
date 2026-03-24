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

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The Aerospike Bin related data.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class BinData {
    /**
     * The {@link ParticleType} of a bin.
     */
    @Nonnull
    private final ParticleType particleType;

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

    /**
     * The src-id of XDR.
     */
    @Nullable
    private final Byte srcId;

    /**
     * The meta info of the list if a {@link #value} is a list.
     */
    @Nullable
    private final ListContext listContext;

    /**
     * The meta info of the map if a {@link #value} is a map.
     */
    @Nullable
    private final MapContext mapContext;

    public BinData(@Nonnull ParticleType particleType, @Nullable Object value) {
        this(particleType, value, null, null, null, null);
    }

    public BinData(@Nonnull ParticleType particleType, @Nullable Object value,
                   @Nullable Long lastUpdateTimeMs) {
        this(particleType, value, lastUpdateTimeMs, null, null, null);
    }

    public BinData(@Nonnull ParticleType particleType, @Nullable Object value,
                   @Nullable Long lastUpdateTimeMs, @Nullable Byte srcId) {
        this(particleType, value, lastUpdateTimeMs, srcId, null, null);
    }
}
