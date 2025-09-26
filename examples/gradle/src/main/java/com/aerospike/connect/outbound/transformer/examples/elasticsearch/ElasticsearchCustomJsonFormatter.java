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
import co.elastic.clients.elasticsearch.core.bulk.CreateOperation;
import com.aerospike.connect.outbound.elasticsearch.ElasticsearchOutboundMetadata;
import com.aerospike.connect.outbound.elasticsearch.format.ElasticsearchOutboundRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.FormatterInput;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ElasticsearchCustomJsonFormatter formats change notification record by adding
 * an extra field named 'created_at' along with the parsed bins.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.elasticsearch.ElasticsearchCustomJsonFormatter
 * </pre>
 */
@Singleton
public class ElasticsearchCustomJsonFormatter
        implements Formatter<ElasticsearchOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(
                    ElasticsearchCustomJsonFormatter.class.getName());

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OutboundRecord<ElasticsearchOutboundMetadata> format(
            @NonNull FormatterInput<ElasticsearchOutboundMetadata> formatterInput) {
        logger.debug("Formatting record {}",
                formatterInput.getRecord().getMetadata().getKey());
        Map<String, Object> bins = formatterInput.getRecord().getBins();
        Map<String, Object> resultMap = new HashMap<>(bins.size() + 1);
        resultMap.putAll(bins);
        resultMap.put("created_at", System.currentTimeMillis());
        return (ElasticsearchOutboundRecord) () -> {
            try {
                Builder builder = new Builder();
                builder.index("my_test_index");
                byte[] value = objectMapper.writeValueAsBytes(resultMap);
                builder.operations(op -> op.create(CreateOperation.of(cob ->
                        cob.document(new PreSerializedJson(value)))
                ));
                return builder.build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
