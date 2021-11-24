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

package com.aerospike.connect.outbound.format;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.esp.EspOutboundMetadata;
import com.aerospike.connect.outbound.pulsar.PulsarOutboundMetadata;
import lombok.NonNull;

import java.util.Map;

/**
 * Format the Aerospike record into an outbound record for the outbound
 * destination.
 *
 * <p>
 * The implementing code should specify the type {@code T} applicable to the
 * outbound destination - like {@link EspOutboundMetadata} for ESP (Event Stream
 * Processing) destination, {@link PulsarOutboundMetadata} for Pulsar
 * destination, etc.
 * </p>
 *
 * @param <T> the metadata associated with the outbound record.
 */
public interface Formatter<T extends OutboundMetadata> {
    /**
     * Format a record into a custom format.
     *
     * <p>
     * The {@code formattedRecord} is an instance of either {@link
     * BytesOutboundRecord} or {@link TextOutboundRecord}. The {@code payload}
     * in the {@code formattedRecord} is {@code null} unless the custom
     * formatter is configured with one of the inbuilt outbound formats - AVRO,
     * FlatJSON, etc in the config. The {@code formattedRecord} is an instance
     * of {@link TextOutboundRecord} only when the custom formatter is
     * configured with the FlatJSON or JSON inbuilt outbound formats.
     * </p>
     *
     * <p>
     * The return types should be instances of
     * <ul>
     *     <li>{@link BytesOutboundRecord} for all the outbound destinations.</li>
     *     <li>{@link TextOutboundRecord} for JMS outbound destination when
     *     the JMS message should be sent as a JMS TextMessage.</li>
     * </ul>
     * </p>
     *
     * @param record          the record to be formatted.
     * @param params          the params for the formatter specified in the
     *                        config file. <bold>WARN:</bold> It is an
     *                        unmodifiable map.
     * @param formattedRecord the formatted record.
     * @return the formatted record.
     *
     * @throws Exception if failed to format the record.
     */
    OutboundRecord<T> format(@NonNull ChangeNotificationRecord record,
                             @NonNull Map<String, Object> params,
                             @NonNull OutboundRecord<T> formattedRecord) throws Exception;
}
