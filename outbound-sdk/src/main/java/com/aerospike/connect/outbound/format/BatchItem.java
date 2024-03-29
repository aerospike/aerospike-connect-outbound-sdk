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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * A batch item passed to the {@link BatchFormatter}.
 *
 * @param <T> the metadata associated with the outbound records.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class BatchItem<T extends OutboundMetadata>
        implements FormatterInput<T> {
    /**
     * @see FormatterInput#getRecord()
     */
    @NonNull
    private ChangeNotificationRecord record;

    /**
     * @see FormatterInput#getFormattedRecord()
     */
    @NonNull
    private OutboundRecord<T> formattedRecord;

    /**
     * @see FormatterInput#getOutboundRoute()
     */
    @NonNull
    private OutboundRoute<?> outboundRoute;
}
