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

package com.aerospike.connect.outbound.transform.examples.jms;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.format.BytesOutboundRecord;
import com.aerospike.connect.outbound.format.DefaultBytesOutboundRecord;
import com.aerospike.connect.outbound.format.DefaultTextOutboundRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.aerospike.connect.outbound.jms.JmsOutboundMetadata;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;

/**
 * JmsMessageTypeFormatter dispatches built-in JSON formatted record as JMS
 * TextMessage or JMS ByteMessage based on config params.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transform.examples.jms.JmsMessageTypeFormatter
 *   payload-format:
 *     mode: json # Format record with built-in JSON format.
 *   params:
 *     asText: true # Should message be dispatched as JMS TextMessage?
 * </pre>
 */
@Singleton
public class JmsMessageTypeFormatter implements Formatter<JmsOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(JmsMessageTypeFormatter.class.getName());

    @Override
    public OutboundRecord<JmsOutboundMetadata> format(
            @NonNull ChangeNotificationRecord record,
            @NonNull Map<String, Object> params,
            @NonNull OutboundRecord<JmsOutboundMetadata> formattedRecord) {
        logger.debug("Formatting record {}", record.getKey());

        byte[] payload =
                ((BytesOutboundRecord<JmsOutboundMetadata>) formattedRecord)
                        .getPayload()
                        .orElseThrow(() -> new IllegalArgumentException(
                                "payload missing, expected json payload"));

        // "asText" should be passed as params in the config.
        if (params.containsKey("asText") && (boolean) params.get("asText")) {
            // Will be dispatched as JMS TextMessage.
            return new DefaultTextOutboundRecord<JmsOutboundMetadata>(
                    payload, formattedRecord.getMediaType(),
                    formattedRecord.getMetadata());
        } else {
            // Will be dispatched as JMS BytesMessage.
            return new DefaultBytesOutboundRecord<JmsOutboundMetadata>(
                    payload, formattedRecord.getMediaType(),
                    formattedRecord.getMetadata());
        }
    }
}
