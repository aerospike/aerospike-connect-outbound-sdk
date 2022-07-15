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
import lombok.ToString;

/**
 * {@link ElasticsearchOutboundRecord} implementation for deleting a document.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ElasticsearchDeleteDocumentOutboundRecord
        implements ElasticsearchOutboundRecord<ElasticsearchOutboundMetadata> {
    /**
     * @return An Elasticsearch index to perform an operation upon.
     */
    private final String index;

    /**
     * @return A metadata associated with the record.
     */
    private final ElasticsearchOutboundMetadata metadata;
}
