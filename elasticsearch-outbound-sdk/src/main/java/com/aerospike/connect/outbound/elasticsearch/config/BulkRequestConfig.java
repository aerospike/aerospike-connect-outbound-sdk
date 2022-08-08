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

package com.aerospike.connect.outbound.elasticsearch.config;

import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.WaitForActiveShards;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.OperationType;
import co.elastic.clients.elasticsearch.core.search.SourceConfigParam;
import com.aerospike.connect.outbound.config.DynamicFieldSource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Config parameters for Elasticsearch
 * {@link co.elastic.clients.elasticsearch.core.BulkRequest}.
 */
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Value
public class BulkRequestConfig {
    /**
     * See {@link BulkRequest#source()}.
     *
     * @param source Config defining how to fetch a source field.
     * @return config defining how to fetch a source field.
     */
    @Nullable
    SourceConfigParam source;

    /**
     * See {@link BulkRequest#sourceExcludes()}.
     *
     * @param sourceExcludes Sources to exclude.
     * @return sources to exclude.
     */
    @JsonProperty("source-excludes")
    List<String> sourceExcludes;

    /**
     * See {@link BulkRequest#sourceIncludes()}.
     *
     * @param sourceIncludes Sources to include.
     * @return sources to include.
     */
    @JsonProperty("source-includes")
    List<String> sourceIncludes;

    /**
     * See {@link BulkRequest#index()}.
     *
     * @param index Index to perform each operation on.
     * @return index to perform each operation on.
     */
    @NonNull
    DynamicFieldSource index;

    /**
     * See {@link BulkRequest#pipeline()}.
     *
     * @param pipeline The pipeline id to preprocess incoming documents with.
     * @return the pipeline id to preprocess incoming documents with.
     */
    @Nullable
    String pipeline;

    /**
     * See {@link BulkRequest#refresh()}.
     *
     * @param refresh Whether to refresh the affected shards to make this
     * operation visible to search.
     * @return whether to refresh the affected shards to make this operation
     * visible to search.
     */
    @Nullable
    Refresh refresh;

    /**
     * See {@link BulkRequest#requireAlias()}.
     *
     * @param requireAlias A require_alias field for all incoming documents.
     * @return a require_alias field for all incoming documents.
     */
    @Nullable
    @JsonProperty("require-alias")
    Boolean requireAlias;

    /**
     * See {@link BulkRequest#routing()}.
     *
     * @param routing Specific routing value.
     * @return specific routing value.
     */
    @Nullable
    DynamicFieldSource routing;

    /**
     * See {@link BulkRequest#timeout()}.
     *
     * @param timeout Explicit operation timeout.
     * @return explicit operation timeout.
     */
    @Nullable
    Time timeout;

    /**
     * See {@link BulkRequest#waitForActiveShards()}.
     *
     * @param waitForActiveShards The number of shard copies that must be active
     * before proceeding with the bulk operation.
     * @return the number of shard copies that must be active before proceeding
     * with the bulk operation.
     */
    @Nullable
    @JsonProperty("wait-for-active-shards")
    WaitForActiveShards waitForActiveShards;

    /**
     * Mapping of an Aerospike XDR's write operation to Elasticsearch
     * {@link OperationType}. {@link OperationType#Index} is the default value.
     *
     * @param writeOperationType The {@link OperationType} to be performed on
     * Elasticsearch.
     * @return The {@link OperationType} to be performed on Elasticsearch.
     */
    @NonNull
    @JsonProperty("write-operation-type")
    OperationType writeOperationType;
}
