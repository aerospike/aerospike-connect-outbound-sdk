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

import co.elastic.clients.elasticsearch.core.BulkRequest;
import com.aerospike.connect.outbound.elasticsearch.ElasticsearchOutboundMetadata;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import lombok.NonNull;

/**
 * An Elasticsearch outbound record containing operations to be performed on
 * Elasticsearch via a {@link BulkRequest.Builder}.
 */
public interface ElasticsearchOutboundRecord
        extends OutboundRecord<ElasticsearchOutboundMetadata> {
    /**
     * A {@link BulkRequest.Builder} containing operations to be performed on
     * Elasticsearch.
     *
     * @return A {@link BulkRequest.Builder}.
     */
    @NonNull
    BulkRequest.Builder getBulkRequestBuilder();

    @Override
    @NonNull
    default String getMediaType() {
        return MediaType.NDJSON;
    }

    @Override
    @NonNull
    default ElasticsearchOutboundMetadata getMetadata() {
        return ElasticsearchOutboundMetadata.INSTANCE;
    }
}
