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

package com.aerospike.connect.outbound.pubsub;

import com.aerospike.connect.outbound.format.OutboundMetadata;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;

/**
 * The metadata associated with the Google Pub/Sub outbound record.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class PubSubOutboundMetadata implements OutboundMetadata {
    /**
     * The attributes of the Google Pub/Sub message.
     */
    @Nullable
    private final Map<String, String> attributes;

    /**
     * The ordering key of the Google Pub/Sub message. Ordering key can be used
     * in Google Pub/Sub only when a regional endpoint is specified in the
     * config for the Aerospike record.
     */
    @Nullable
    private final String orderingKey;

    /**
     * Get the attributes of the Google Pub/Sub message.
     *
     * @return the attributes.
     */
    public Optional<Map<String, String>> getAttributes() {
        return Optional.ofNullable(attributes);
    }

    /**
     * Get the ordering key of the Google Pub/Sub message. Ordering key can be
     * used in Google Pub/Sub only when a regional endpoint is specified in the
     * config for the Aerospike record.
     *
     * @return the ordering key.
     */
    public Optional<String> getOrderingKey() {
        return Optional.ofNullable(orderingKey);
    }
}
