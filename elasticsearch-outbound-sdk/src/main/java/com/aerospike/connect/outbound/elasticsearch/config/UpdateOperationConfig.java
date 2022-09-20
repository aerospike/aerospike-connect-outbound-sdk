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

import co.elastic.clients.elasticsearch.core.bulk.OperationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.annotation.Nullable;

/**
 * {@link OperationConfig} implementation defining extra config for
 * Elasticsearch's {@link OperationType#Update} operation.
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Value
public class UpdateOperationConfig implements OperationConfig {
    /**
     * Specify how many times should the operation be retried when a conflict
     * occurs.
     *
     * @param retryOnConflict The no. of retries to perform when conflict
     * occurs.
     * @return The no. of retries to perform when conflict occurs.
     */
    @Nullable
    @JsonProperty("retry-on-conflict")
    Integer retryOnConflict;

    /**
     * Set to true to use the contents of 'doc' as the value of 'upsert'.
     *
     * @param docAsUpsert A boolean flag to use doc as upsert value.
     * @return A boolean flag to use doc as upsert value.
     */
    @Nullable
    @JsonProperty("doc-as-upsert")
    Boolean docAsUpsert;
}
