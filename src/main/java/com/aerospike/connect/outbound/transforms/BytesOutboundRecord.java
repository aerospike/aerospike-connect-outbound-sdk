/*
 *
 *  Copyright 2012-2021 Aerospike, Inc.
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

package com.aerospike.connect.outbound.transforms;

import javax.annotation.Nullable;

/**
 * BytesOutboundRecord represents an outbound record with payload as bytes.
 *
 * @param <T> the type of metadata associated with the outbound record.
 * @see DefaultBytesOutboundRecord
 */
public interface BytesOutboundRecord<T extends OutboundMetadata>
        extends OutboundRecord<T> {
    /**
     * Get the payload associated with the outbound record which is sent to the
     * outbound destination.
     *
     * @return the payload associated with the outbound record.
     */
    @Nullable
    byte[] getPayload();
}
