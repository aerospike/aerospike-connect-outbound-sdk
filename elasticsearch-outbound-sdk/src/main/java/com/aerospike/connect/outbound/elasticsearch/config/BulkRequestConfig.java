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
import co.elastic.clients.elasticsearch.core.search.SourceConfigParam;
import com.aerospike.connect.outbound.config.DynamicFieldSource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Config parameters for Elasticsearch
 * {@link co.elastic.clients.elasticsearch.core.BulkRequest}.
 */
@AllArgsConstructor
@Getter
public class BulkRequestConfig {
    /**
     * See {@link BulkRequest#source()}.
     */
    @Nullable
    private final SourceConfigParam source;

    /**
     * See {@link BulkRequest#sourceExcludes()}.
     */
    @JsonProperty("source-excludes")
    private final List<String> sourceExcludes;

    /**
     * See {@link BulkRequest#sourceIncludes()}.
     */
    @JsonProperty("source-includes")
    private final List<String> sourceIncludes = emptyList();

    /**
     * See {@link BulkRequest#index()}.
     */
    @Nonnull
    private final DynamicFieldSource index;

    /**
     * See {@link BulkRequest#pipeline()}.
     */
    @Nullable
    private final String pipeline;

    /**
     * See {@link BulkRequest#refresh()}.
     */
    @Nullable
    private final Refresh refresh;

    /**
     * See {@link BulkRequest#requireAlias()}.
     */
    @Nullable
    @JsonProperty("require-alias")
    private final Boolean requireAlias;

    /**
     * See {@link BulkRequest#routing()}.
     */
    @Nullable
    private final DynamicFieldSource routing;

    /**
     * See {@link BulkRequest#timeout()}.
     */
    @Nullable
    private final Time timeout;

    /**
     * See {@link BulkRequest#waitForActiveShards()}.
     */
    @Nullable
    @JsonProperty("wait-for-active-shards")
    private final WaitForActiveShards waitForActiveShards;
}
