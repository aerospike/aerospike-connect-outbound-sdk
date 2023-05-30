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

import co.elastic.clients.elasticsearch.core.BulkRequest.Builder;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import com.aerospike.connect.outbound.elasticsearch.ElasticsearchOutboundMetadata;
import com.aerospike.connect.outbound.elasticsearch.format.ElasticsearchOutboundRecord;
import com.aerospike.connect.outbound.format.BytesOutboundRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.FormatterConfig;
import com.aerospike.connect.outbound.format.FormatterInput;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.aerospike.connect.outbound.format.SkipOutboundRecord;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

/**
 * ElasticsearchSkipFormatter skips record exceeding a configured generation
 * number.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.elasticsearch.ElasticsearchSkipFormatter
 *   params:
 *     genNumber: 100
 *   payload-format:
 *     mode: json # Format record with built-in JSON format.
 * </pre>
 */
@Singleton
public class ElasticsearchSkipFormatter
        implements Formatter<ElasticsearchOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(ElasticsearchSkipFormatter.class.getName());

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    @Inject
    public ElasticsearchSkipFormatter(FormatterConfig routerConfig) {
        configParams = routerConfig.getParams();
    }

    @Override
    public OutboundRecord<ElasticsearchOutboundMetadata> format(
            @NonNull FormatterInput<ElasticsearchOutboundMetadata> formatterInput) {
        logger.debug("Formatting record {}",
                formatterInput.getRecord().getMetadata().getKey());

        // Record generation is not shipped by Aerospike XDR versions before
        // v5.0.0.
        Optional<Integer> generation =
                formatterInput.getRecord().getMetadata().getGeneration();

        // "genNumber" is to be set in params option of the Elasticsearch
        // formatter config.
        if (generation.isPresent() &&
                generation.get() > (int) configParams.get("genNumber")) {
            logger.debug("Skipping record {}",
                    formatterInput.getRecord().getMetadata().getKey());
            return new SkipOutboundRecord<>(MediaType.OCTET_STREAM,
                    formatterInput.getFormattedRecord().getMetadata());
        }

        // Return built-in JSON formatted record.
        return (ElasticsearchOutboundRecord) () -> {
            try {
                Builder builder = new Builder();
                builder.index("my_test_index");
                @SuppressWarnings({"rawtypes", "OptionalGetWithoutIsPresent"})
                byte[] value = (byte[]) ((BytesOutboundRecord)
                        formatterInput.getFormattedRecord()).getPayload().get();
                builder.operations(op -> op.index(IndexOperation.of(iob -> {
                            iob.document(new PreSerializedJson(value));
                            iob.id("3642380");
                            return iob;
                        })
                ));
                return builder.build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
