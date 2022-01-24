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

package com.aerospike.connect.outbound.transformer.examples.kafka;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.FormatterConfig;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.aerospike.connect.outbound.format.SkipOutboundRecord;
import com.aerospike.connect.outbound.kafka.KafkaOutboundMetadata;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

/**
 * KafkaSkipFormatter skips record exceeding a configured generation number.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.kafka.KafkaSkipFormatter
 *   params:
 *     genNumber: 100
 *   payload-format:
 *     mode: json # Format record with built-in JSON format.
 * </pre>
 */
@Singleton
public class KafkaSkipFormatter implements Formatter<KafkaOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(KafkaSkipFormatter.class.getName());

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    @Inject
    public KafkaSkipFormatter(FormatterConfig routerConfig) {
        this.configParams = routerConfig.getParams();
    }

    @Override
    public OutboundRecord<KafkaOutboundMetadata> format(
            @NonNull ChangeNotificationRecord record,
            @NonNull OutboundRecord<KafkaOutboundMetadata> formattedRecord) {
        logger.debug("Formatting record {}", record.getKey());

        // Record generation is not shipped by Aerospike XDR versions before
        // v5.0.0.
        Optional<Integer> generation = record.getGeneration();

        // "genNumber" is to be set in params option of the Kafka formatter
        // config.
        if (generation.isPresent() &&
                generation.get() > (int) configParams.get("genNumber")) {
            logger.debug("Skipping record {}", record.getKey());
            return new SkipOutboundRecord<>(MediaType.OCTET_STREAM,
                    formattedRecord.getMetadata());
        }

        // Return built-in JSON formatted record.
        return formattedRecord;
    }
}
