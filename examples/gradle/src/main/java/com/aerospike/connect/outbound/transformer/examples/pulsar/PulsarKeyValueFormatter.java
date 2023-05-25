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

package com.aerospike.connect.outbound.transformer.examples.pulsar;

import com.aerospike.connect.outbound.format.DefaultTextOutboundRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.FormatterConfig;
import com.aerospike.connect.outbound.format.FormatterInput;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.aerospike.connect.outbound.pulsar.PulsarOutboundMetadata;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Map;

/**
 * PulsarKeyValueFormatter formats change notification record as bin value pairs
 * separated by newlines.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.pulsar.PulsarKeyValueFormatter
 *   params:
 *     separator: ":"
 * </pre>
 */
@Singleton
public class PulsarKeyValueFormatter
        implements Formatter<PulsarOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(PulsarKeyValueFormatter.class.getName());

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    @Inject
    public PulsarKeyValueFormatter(FormatterConfig formatterConfig) {
        configParams = formatterConfig.getParams();
    }

    @Override
    public OutboundRecord<PulsarOutboundMetadata> format(
            @NonNull FormatterInput<PulsarOutboundMetadata> formatterInput)
            throws Exception {
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

        return new DefaultTextOutboundRecord<>(
                payloadBuilder.toString().getBytes(), MediaType.OCTET_STREAM,
                formatterInput.getFormattedRecord().getMetadata(),
                Collections.emptySet());
    }
}
