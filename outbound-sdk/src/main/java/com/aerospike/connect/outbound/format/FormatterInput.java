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
import com.aerospike.connect.outbound.routing.OutboundRoute;
import lombok.NonNull;

/**
 * Input to {@link Formatter}.
 *
 * @param <T> the metadata associated with the outbound records.
 */
public interface FormatterInput<T extends OutboundMetadata> {
    /**
     * @return the change notification record shipped by Aerospike XDR
     */
    @NonNull
    ChangeNotificationRecord getRecord();

    /**
     * @return a {@code formattedRecord} which is an instance of either
     * {@link BytesOutboundRecord} or {@link TextOutboundRecord}. The
     * {@code payload} in the {@code formattedRecord} is {@code null} unless the
     * {@code `payload-format`} config in the custom formatter is set to one of
     * the built-in outbound formats - AVRO, FlatJSON, etc. The
     * {@code formattedRecord} is an instance of {@link TextOutboundRecord} only
     * when the custom formatter is configured with the FlatJSON or JSON
     * built-in outbound formats.
     */
    @NonNull
    OutboundRecord<T> getFormattedRecord();

    /**
     * @return the route this item will be dispatched
     */
    @NonNull
    OutboundRoute<?> getOutboundRoute();
}
