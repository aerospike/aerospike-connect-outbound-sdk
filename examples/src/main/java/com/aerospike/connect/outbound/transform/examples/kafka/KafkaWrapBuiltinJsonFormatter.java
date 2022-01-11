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

package com.aerospike.connect.outbound.transform.examples.kafka;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.format.BytesOutboundRecord;
import com.aerospike.connect.outbound.format.DefaultBytesOutboundRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.aerospike.connect.outbound.kafka.KafkaOutboundMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * KafkaWrapBuiltinJsonFormatter wraps the built-in JSON format with additional
 * metadata.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transform.examples.kafka.KafkaWrapBuiltinJsonFormatter
 *   payload-format:
 *     mode: json # Format record with built-in JSON format.
 * </pre>
 */
@Singleton
public class KafkaWrapBuiltinJsonFormatter
        implements Formatter<KafkaOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(
                    KafkaWrapBuiltinJsonFormatter.class.getName());
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OutboundRecord<KafkaOutboundMetadata> format(
            @NonNull ChangeNotificationRecord record,
            @NonNull OutboundRecord<KafkaOutboundMetadata> formattedRecord)
            throws JsonProcessingException {
        logger.debug("Formatting record {}", record.getKey());

        byte[] payload =
                ((BytesOutboundRecord<KafkaOutboundMetadata>) formattedRecord)
                        .getPayload()
                        .orElseThrow(() -> new IllegalArgumentException(
                                "payload missing, expected json payload"));

        // Wrap JSON with timestamp.
        Map<String, Object> jsonRecord = new HashMap<String, Object>();
        jsonRecord.put("timestamp", new Date().getTime());
        jsonRecord.put("recordPayload", new String(payload));
        byte[] jsonRecordPayload = objectMapper.writeValueAsBytes(jsonRecord);

        return new DefaultBytesOutboundRecord<KafkaOutboundMetadata>(
                jsonRecordPayload, MediaType.JSON,
                formattedRecord.getMetadata());
    }
}
