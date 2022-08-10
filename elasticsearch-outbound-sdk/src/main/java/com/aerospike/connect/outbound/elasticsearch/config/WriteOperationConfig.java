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
import lombok.Value;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * {@link OperationConfig} implementation defining extra config for
 * Elasticsearch's {@link OperationType#Create} and {@link OperationType#Index}
 * operations.
 */
@Value
public class WriteOperationConfig implements OperationConfig {
    /**
     * Define custom mappings that can be applied to dynamically added fields
     * based on the matching condition. Refer <a
     * href="https://www.elastic.co/guide/en/elasticsearch/reference/current
     * /dynamic-templates.html">this</a> for more details.
     *
     * @param dynamicTemplates The dynamicTemplates.
     * @return The dynamicTemplates.
     */
    @Nullable
    @JsonProperty("dynamic-templates")
    Map<String, String> dynamicTemplates;
}
