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

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.transformer.SkipChangeNotificationRecord;
import com.aerospike.connect.outbound.transformer.Transformer;
import com.aerospike.connect.outbound.transformer.TransformerConfig;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

/**
 * ElasticsearchSkipTransformer skips records exceeding a configured generation
 * number.
 *
 * <p>
 * A snippet of a config for this transformer can be
 * <pre>
 * custom-transformer:
 *   class: com.aerospike.connect.outbound.transformer.examples.elasticsearch.ElasticsearchSkipTransformer
 *   params:
 *     genNumber: 100
 * </pre>
 */
public class ElasticsearchSkipTransformer implements Transformer {
    private final static Logger logger =
            LoggerFactory.getLogger(
                    ElasticsearchSkipTransformer.class.getName());

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    @Inject
    public ElasticsearchSkipTransformer(TransformerConfig routerConfig) {
        configParams = routerConfig.getParams();
    }

    @Override
    public ChangeNotificationRecord transform(
            @NonNull ChangeNotificationRecord record) {
        // Record generation is not shipped by Aerospike XDR versions before
        // v5.0.0.
        Optional<Integer> generation = record.getMetadata().getGeneration();

        // "genNumber" is to be set in params option of the Elasticsearch
        // transformer config.
        if (generation.isPresent() &&
                generation.get() > (int) configParams.get("genNumber")) {
            logger.debug("Skipping record {}", record.getMetadata().getKey());
            return new SkipChangeNotificationRecord();
        }

        return record;
    }
}
