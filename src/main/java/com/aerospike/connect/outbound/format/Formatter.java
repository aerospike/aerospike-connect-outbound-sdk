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

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.esp.EspOutboundMetadata;
import com.aerospike.connect.outbound.pulsar.PulsarOutboundMetadata;
import lombok.NonNull;

/**
 * Format the Aerospike record into an outbound record for the outbound
 * destination.
 *
 * <p>
 * The implementing code should specify the type {@code T} applicable to the
 * outbound destination - like {@link EspOutboundMetadata} for ESP (Event Stream
 * Processing) destination, {@link PulsarOutboundMetadata} for Pulsar
 * destination, etc.
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
     * formatter is configured with one of the built-in outbound formats - AVRO,
     * FlatJSON, etc. in the config. The {@code formattedRecord} is an instance
     * of {@link TextOutboundRecord} only when the custom formatter is
     * configured with the FlatJSON or JSON built-in outbound formats.
     *
     * <p>
     * The return types should be instances of
     * <ul>
     *     <li>{@link BytesOutboundRecord} for all the outbound destinations.
     *     For JMS outbound destination the message will be sent as a JMS
     *     BytesMessage.
     *     </li>
     *     <li>{@link TextOutboundRecord} for JMS outbound destination when
     *     the JMS message should be sent as a JMS TextMessage.</li>
     *     <li>{@link SkipOutboundRecord} to skip dispatching the change
     *     notification record to the outbound destination.</li>
     * </ul>
     *
     * <p>
     * When an exception is thrown by this method, the record is acknowledged
     * with temporary error to Aerospike XDR change notification. Aerospike XDR
     * change notification will resend the change notification record on a
     * temporary error.
     *
     * @param record          the record to be formatted.
     * @param formattedRecord the formatted record.
     * @return the formatted record.
     * @throws Exception if failed to format the record. The record is
     *                   acknowledged with temporary error to Aerospike XDR
     *                   change notification.
     */
    OutboundRecord<T> format(@NonNull ChangeNotificationRecord record,
                             @NonNull OutboundRecord<T> formattedRecord)
            throws Exception;
}
