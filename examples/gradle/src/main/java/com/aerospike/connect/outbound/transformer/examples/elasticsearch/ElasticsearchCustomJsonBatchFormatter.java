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
import com.aerospike.connect.outbound.format.BatchFormatter;
import com.aerospike.connect.outbound.format.BatchItem;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ElasticsearchCustomJsonBatchFormatter formats change notification records by
 * adding an extra field named 'created_at' along with the parsed bins. Also,
 * setting the index at operation level if it can be derived.
 *
 * <p>
 * A snippet of a config for this batch-formatter can be
 * <pre>
 * format:
 *   mode: custom
 *   batch-formatter-class: com.aerospike.connect.outbound.transformer.examples.elasticsearch.ElasticsearchCustomJsonBatchFormatter
 * </pre>
 */
@Singleton
public class ElasticsearchCustomJsonBatchFormatter
        implements BatchFormatter<ElasticsearchOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(
                    ElasticsearchCustomJsonBatchFormatter.class.getName());

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<OutboundRecord<ElasticsearchOutboundMetadata>> format(
            @NonNull List<BatchItem<ElasticsearchOutboundMetadata>> batchItems)
            throws Exception {
        ElasticsearchOutboundRecord elasticsearchOutboundRecord =
                getElasticsearchOutboundRecord(batchItems);
        List<OutboundRecord<ElasticsearchOutboundMetadata>>
                outboundRecords = new ArrayList<>();
        outboundRecords.add(elasticsearchOutboundRecord);
        return outboundRecords;
    }

    @NonNull
    private ElasticsearchOutboundRecord getElasticsearchOutboundRecord(
            @NonNull List<BatchItem<ElasticsearchOutboundMetadata>> batchItems) {
        long currentTimeMillis = System.currentTimeMillis();
        return () -> {
            Builder builder = new Builder();
            // This is the default index for all bulk operations if we don't
            // override at the operation level.
            builder.index("my_test_index");
            batchItems.stream().map(BatchItem::getRecord).forEach(record -> {
                        logger.debug("Formatting record {}",
                                record.getMetadata().getKey());
                        try {
                            Map<String, Object> bins = record.getBins();
                            Map<String, Object> resultMap =
                                    new HashMap<>(bins.size() + 1);
                            resultMap.putAll(bins);
                            resultMap.put("created_at", currentTimeMillis);
                            byte[] value =
                                    objectMapper.writeValueAsBytes(resultMap);
                            builder.operations(
                                    op -> op.create(CreateOperation.of(cob -> {
                                                cob.document(
                                                        new PreSerializedJson(value));
                                                Object esIndex = bins.get("es_index");
                                                if (esIndex instanceof String) {
                                                    // Set index for this Create
                                                    // operation.
                                                    cob.index((String) esIndex);
                                                }
                                                return cob;
                                            })
                                    ));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            return builder.build();
        };
    }
}
