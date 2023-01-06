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

package com.aerospike.connect.outbound.format;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

/**
 * A default implementation of {@link BytesOutboundRecord}.
 *
 * @param <T> the type of metadata associated with the outbound record.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class DefaultBytesOutboundRecord<T extends OutboundMetadata>
        implements BytesOutboundRecord<T> {
    @Nullable
    private final byte[] payload;

    @NonNull
    private final String mediaType;

    @NonNull
    private final T metadata;

    @Nullable
    private final Set<Object> ignoreErrors;

    @Override
    public Optional<byte[]> getPayload() {
        return Optional.ofNullable(payload);
    }
}
