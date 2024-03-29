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

package com.aerospike.connect.outbound.transformer.examples.pubsub;

import com.aerospike.connect.outbound.format.DefaultTextOutboundRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.FormatterConfig;
import com.aerospike.connect.outbound.format.FormatterInput;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.aerospike.connect.outbound.pubsub.PubSubOutboundMetadata;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Map;

/**
 * PubSubKeyValueFormatter formats change notification record as bin value pairs
 * separated by newlines.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * # OPTIONAL. If configured will be passed in PubSubOutboundMetadata to PubSubKeyValueFormatter.
 * attributes:
 *   colour:
 *     mode: static
 *     value: RED
 *
 * regional-endpoint: us-east1-pubsub.googleapis.com:443
 *
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.pubsub.PubSubKeyValueFormatter
 * </pre>
 */
@Singleton
public class PubSubKeyValueFormatter
        implements Formatter<PubSubOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(PubSubKeyValueFormatter.class.getName());

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    @Inject
    public PubSubKeyValueFormatter(FormatterConfig formatterConfig) {
        configParams = formatterConfig.getParams();
    }

    @Override
    public OutboundRecord<PubSubOutboundMetadata> format(
            @NonNull FormatterInput<PubSubOutboundMetadata> formatterInput) {
        logger.debug("Formatting record {}",
                formatterInput.getRecord().getMetadata().getKey());

        // Only write string bins.
        StringBuilder payloadBuilder = new StringBuilder();
        String separator =
                (String) configParams.getOrDefault("separator", ":");
        for (Map.Entry<String, Object> bin : formatterInput.getRecord()
                .getBins().entrySet()) {
            if (bin.getValue() instanceof String) {
                payloadBuilder.append(bin.getKey());
                payloadBuilder.append(separator);
                payloadBuilder.append(bin.getValue());
                payloadBuilder.append(System.lineSeparator());
            }
        }

        // If attribute colour is present add it to the payload as well.
        formatterInput.getFormattedRecord().getMetadata().getAttributes()
                .ifPresent(attributes -> {
                    if (attributes.containsKey("colour")) {
                        payloadBuilder.append("colour");
                        payloadBuilder.append(separator);
                        payloadBuilder.append(attributes.get("colour"));
                        payloadBuilder.append(System.lineSeparator());
                    }
                });

        // Add ordering key. "regional-endpoint" should be configured for this
        // record in the config.
        String orderingKey = "CustomFormatter";
        PubSubOutboundMetadata metadata = new PubSubOutboundMetadata(
                formatterInput.getFormattedRecord().getMetadata()
                        .getAttributes()
                        .orElse(null),
                orderingKey);

        return new DefaultTextOutboundRecord<>(
                payloadBuilder.toString().getBytes(), MediaType.OCTET_STREAM,
                metadata, Collections.emptySet());
    }
}
