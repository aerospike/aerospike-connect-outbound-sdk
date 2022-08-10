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

package com.aerospike.connect.outbound.elasticsearch;

import co.elastic.clients.elasticsearch.core.bulk.OperationType;
import com.aerospike.connect.outbound.elasticsearch.config.OperationConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.Nullable;

/**
 * Mapping of an Aerospike XDR's write operation to Elasticsearch
 * {@link OperationType} with some optional {@link OperationConfig}.
 */
@Value
public class AerospikeWriteOperationMapping {
    /**
     * An Elasticsearch's {@link OperationType} to map an incoming XDR record
     * to.
     */
    @JsonProperty("operation-type")
    @NonNull
    OperationType operationType;

    /**
     * An optional {@link OperationConfig} for this mapping.
     */
    @JsonProperty("operation-config")
    @Nullable
    OperationConfig operationConfig;
}
