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

package com.aerospike.connect.outbound.transform.examples.pulsar;

import com.aerospike.connect.outbound.ChangeNotificationMetadata;
import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.transform.Transformer;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * PulsarRecordTransformer updates generation and adds bins passed in the config
 * to the change notification record.
 *
 * <p>
 * A snippet of a config for this transformer can be
 * <pre>
 * custom-transform:
 *   class: com.aerospike.connect.outbound.transform.examples.pulsar.PulsarRecordTransformer
 *   params:
 *     bins:
 *       colour: RED
 *       shade: light
 * </pre>
 */
@Singleton
public class PulsarRecordTransformer implements Transformer {
    private final static Logger logger =
            LoggerFactory.getLogger(PulsarRecordTransformer.class.getName());

    @Override
    public ChangeNotificationRecord transform(
            @NonNull ChangeNotificationRecord record,
            @NonNull Map<String, Object> params) {
        // Increment generation in metadata.
        Integer generation = record.getGeneration().isPresent() ?
                record.getGeneration().get() + 1 : null;

        logger.debug("Updated generation of record {} to {}", record.getKey(),
                generation);

        ChangeNotificationMetadata metadata =
                new ChangeNotificationMetadata(record.getKey(),
                        record.getOperation(), generation,
                        record.getLastUpdateTimeMs().orElse(null),
                        record.getExpiryTime().orElse(null));

        // record.getBins() is immutable, create a copy.
        Map<String, Object> bins = new HashMap<>(record.getBins());

        // Add bins passed as params in config.
        if (params.containsKey("bins")) {
            @SuppressWarnings("unchecked") Map<String, Object> paramBins =
                    (Map<String, Object>) params.get("bins");
            bins.putAll(paramBins);
        }

        return new ChangeNotificationRecord(metadata, bins);
    }
}
