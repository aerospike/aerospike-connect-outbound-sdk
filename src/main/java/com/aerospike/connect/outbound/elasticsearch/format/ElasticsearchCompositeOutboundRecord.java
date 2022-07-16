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

package com.aerospike.connect.outbound.elasticsearch.format;

import com.aerospike.connect.outbound.elasticsearch.ElasticsearchOutboundMetadata;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

/**
 * Aggregate {@link ElasticsearchOutboundRecord}s to send them as an
 * Elasticsearch bulk request.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ElasticsearchCompositeOutboundRecord
        implements ElasticsearchOutboundRecord<ElasticsearchOutboundMetadata> {
    /**
     * A list of {@link ElasticsearchOutboundRecord}s to be sent in a bulk
     * request. All the {@link ElasticsearchOutboundRecord}s should belong to
     * the same index.
     *
     * @return A list of {@link ElasticsearchOutboundRecord}s.
     */
    private final List<ElasticsearchOutboundRecord<ElasticsearchOutboundMetadata>>
            outboundRecords;

    @Override
    public @NonNull String getIndex() {
        throw new RuntimeException("getIndex should not be called on " +
                ElasticsearchCompositeOutboundRecord.class + "instance");
    }

    @Override
    public @NonNull ElasticsearchOutboundMetadata getMetadata() {
        // Composite records have multiple input record, so we can't supply
        // metadata.
        throw new RuntimeException("getMetadata should not be called on " +
                ElasticsearchCompositeOutboundRecord.class + "instance");
    }
}
