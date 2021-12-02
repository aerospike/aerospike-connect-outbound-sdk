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

package com.aerospike.connect.outbound.transform.examples.pubsub;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.format.BytesOutboundRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.aerospike.connect.outbound.pubsub.PubSubOutboundMetadata;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * An identity formatter.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transform.examples.pubsub.PubSubIdentityFormatter
 *   record-format:
 *     mode: json # Format record with built-in JSON format.
 * </pre>
 * </p>
 */
public class PubSubIdentityFormatter
        implements Formatter<PubSubOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(PubSubIdentityFormatter.class.getName());

    @Override
    public OutboundRecord<PubSubOutboundMetadata> format(
            @NonNull ChangeNotificationRecord record,
            @NonNull Map<String, Object> params,
            @NonNull OutboundRecord<PubSubOutboundMetadata> formattedRecord) {
        if (logger.isDebugEnabled()) {
            ((BytesOutboundRecord<PubSubOutboundMetadata>) formattedRecord)
                    .getPayload().ifPresent(payload ->
                            logger.debug("Record {} is formatted to JSON {}",
                                    record, new String(payload))
                    );
        }

        // Return the JSON formatted record.
        return formattedRecord;
    }
}
