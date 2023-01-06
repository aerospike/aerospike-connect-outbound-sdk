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

import com.aerospike.connect.outbound.esp.EspOutboundMetadata;
import com.aerospike.connect.outbound.pulsar.PulsarOutboundMetadata;
import lombok.NonNull;

import java.util.List;

/**
 * Format the Aerospike records into an outbound records for the outbound
 * destination.
 *
 * <p>
 * The implementing code should specify the type {@code T} applicable to the
 * outbound destination - like {@link EspOutboundMetadata} for ESP (Event Stream
 * Processing) destination, {@link PulsarOutboundMetadata} for Pulsar
 * destination, etc.
 *
 * @param <T> the metadata associated with the outbound records.
 */
public interface BatchFormatter<T extends OutboundMetadata> {
    /**
     * Format records into a custom format.
     *
     * <p>
     * This interface is identical to the {@link Formatter} interface. Here it
     * accepts a collection of {@link BatchItem} to process those records in a
     * batch. Records in this batch can generate multiple
     * {@link OutboundRecord}s as well.
     *
     * <p>
     * The return type should be a {@link List} containing instances of
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
     * When an exception is thrown by this method, all the records in the
     * batch are acknowledged with temporary error to Aerospike XDR change
     * notification. Aerospike XDR change notification will resend all the
     * change notification records in the batch on a temporary error.
     *
     * @param batchItems the list of {@link BatchItem}s containing the record to
     *                   be formatted and the formatted record
     * @return the formatted records.
     * @throws Exception if failed to format the records. The records are
     *                   acknowledged with temporary error to Aerospike XDR
     *                   change notification.
     */
    List<OutboundRecord<T>> format(@NonNull List<BatchItem<T>> batchItems)
            throws Exception;
}
