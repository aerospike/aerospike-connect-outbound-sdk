/*
 *
 *  Copyright 2012-2025 Aerospike, Inc.
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

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.Set;

/**
 * A default implementation of {@link TextOutboundRecord}.
 *
 * @param <T> the type of metadata associated with the outbound record.
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public class DefaultTextOutboundRecord<T extends OutboundMetadata>
        extends DefaultBytesOutboundRecord<T>
        implements TextOutboundRecord<T> {
    public DefaultTextOutboundRecord(@Nullable byte[] payload,
                                     @NonNull String mediaType,
                                     @NonNull T metadata,
                                     @Nullable Set<Object> ignoreErrors) {
        super(payload, mediaType, metadata, ignoreErrors);
    }
}
