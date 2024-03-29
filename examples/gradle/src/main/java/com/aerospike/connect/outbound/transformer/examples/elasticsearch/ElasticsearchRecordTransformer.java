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

package com.aerospike.connect.outbound.transformer.examples.elasticsearch;

import com.aerospike.connect.outbound.ChangeNotificationMetadata;
import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.transformer.Transformer;
import com.aerospike.connect.outbound.transformer.TransformerConfig;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * ElasticsearchRecordTransformer updates generation and adds bins passed in the
 * config to the change notification record.
 *
 * <p>
 * A snippet of a config for this transformer can be
 * <pre>
 * custom-transformer:
 *   class: com.aerospike.connect.outbound.transformer.examples.elasticsearch.ElasticsearchRecordTransformer
 *   params:
 *     bins:
 *       colour: RED
 *       shade: light
 * </pre>
 */
@Singleton
public class ElasticsearchRecordTransformer implements Transformer {
    private final static Logger logger =
            LoggerFactory.getLogger(ElasticsearchRecordTransformer.class.getName());

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    @Inject
    public ElasticsearchRecordTransformer(TransformerConfig transformerConfig) {
        configParams = transformerConfig.getParams();
    }

    @Override
    public ChangeNotificationRecord transform(
            @NonNull ChangeNotificationRecord record) {
        // Increment generation in metadata.
        Integer generation = record.getMetadata().getGeneration().isPresent() ?
                record.getMetadata().getGeneration().get() + 1 : null;

        logger.debug("Updated generation of record {} to {}",
                record.getMetadata().getKey(), generation);

        ChangeNotificationMetadata metadata =
                new ChangeNotificationMetadata(record.getMetadata().getKey(),
                        record.getMetadata().getOperation(), generation,
                        record.getMetadata().getLastUpdateTimeMs().orElse(null),
                        record.getMetadata().getExpiryTime().orElse(null),
                        record.getMetadata().getRecordExistsAction(),
                        record.getMetadata().getGenerationPolicy());

        // record.getBins() is immutable, create a copy.
        Map<String, Object> bins = new HashMap<>(record.getBins());

        // Add bins passed as params in config.
        if (configParams.containsKey("bins")) {
            @SuppressWarnings("unchecked") Map<String, Object> paramBins =
                    (Map<String, Object>) configParams.get("bins");
            bins.putAll(paramBins);
        }

        return new ChangeNotificationRecord(metadata, bins);
    }
}
