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

package com.aerospike.connect.outbound.kafka;

import com.aerospike.connect.outbound.format.OutboundMetadata;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * The metadata associated with the Kafka outbound record.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class KafkaOutboundMetadata implements OutboundMetadata {
    /**
     * The Kafka record key. If <code>null</code> then the Aerospike record
     * digest will be used as the Kafka record key.
     */
    @Nullable
    private final byte[] key;


    /**
     * Get the key of the Kafka record. If missing then the Aerospike record
     * digest will be used as the Kafka record key.
     *
     * @return the key of the Kafka record.
     */
    public Optional<byte[]> getKey() {
        return Optional.ofNullable(key);
    }
}
