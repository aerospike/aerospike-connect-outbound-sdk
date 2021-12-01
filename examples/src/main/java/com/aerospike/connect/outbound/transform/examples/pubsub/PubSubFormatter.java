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
import com.aerospike.connect.outbound.format.DefaultTextOutboundRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.aerospike.connect.outbound.pubsub.PubSubOutboundMetadata;
import com.google.protobuf.ByteString;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;

/**
 * Format incoming change notification record as key value pairs.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * # OPTIONAL. If configured will be passed in PubSubOutboundMetadata to PubSubFormatter.
 * attributes:
 *   colour:
 *     mode: static
 *     value: RED
 *
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transform.examples.pubsub.PubSubFormatter
 * </pre>
 * </p>
 */
@Singleton
public class PubSubFormatter implements Formatter<PubSubOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(PubSubFormatter.class.getName());

    @Override
    public OutboundRecord<PubSubOutboundMetadata> format(
            @NonNull ChangeNotificationRecord record,
            @NonNull Map<String, Object> params,
            @NonNull OutboundRecord<PubSubOutboundMetadata> formattedRecord) {
        logger.debug("Formatting record {}", record.getKey());

        // Only write string bins.
        StringBuilder payloadBuilder = new StringBuilder();
        for (Map.Entry<String, Object> bin : record.getBins().entrySet()) {
            if (bin.getValue() instanceof String) {
                payloadBuilder.append(bin.getKey());
                payloadBuilder.append(":");
                payloadBuilder.append(bin.getValue());
                payloadBuilder.append("\n");
            }
        }

        // If attribute colour is present add it to the payload as well.
        Map<String, String> attributes = formattedRecord.getMetadata().getAttributes();
        if (attributes != null && attributes.containsKey("colour")) {
            payloadBuilder.append("colour");
            payloadBuilder.append(":");
            payloadBuilder.append(attributes.get("colour"));
            payloadBuilder.append("\n");
        }

        // Add ordering key.
        ByteString orderingKey = ByteString.copyFromUtf8("CustomFormatter");
        PubSubOutboundMetadata metadata = new PubSubOutboundMetadata(attributes, orderingKey);

        return new DefaultTextOutboundRecord<PubSubOutboundMetadata>(
                payloadBuilder.toString().getBytes(), MediaType.OCTET_STREAM,
                metadata);
    }
}
