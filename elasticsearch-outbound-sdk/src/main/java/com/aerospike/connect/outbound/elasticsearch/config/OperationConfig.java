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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * A marker interface for specifying extra config for Elasticsearch's
 * {@link OperationType#Create}, {@link OperationType#Index} and
 * {@link OperationType#Update} operations.
 */
@JsonTypeInfo(use = Id.NAME, property = "operation-type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateOperationConfig.class,
                name = "create"),
        @JsonSubTypes.Type(value = IndexOperationConfig.class,
                name = "index"),
        @JsonSubTypes.Type(value = UpdateOperationConfig.class,
                name = "update")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface OperationConfig {
}
