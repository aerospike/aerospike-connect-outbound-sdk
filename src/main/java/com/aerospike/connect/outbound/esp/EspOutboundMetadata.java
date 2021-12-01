/*
 *
 *  Copyright 2012-2021 Aerospike, Inc.
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

package com.aerospike.connect.outbound.esp;

import com.aerospike.connect.outbound.format.OutboundMetadata;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Map;

/**
 * The metadata associated with the ESP (Event Stream Processing) outbound
 * record.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class EspOutboundMetadata implements OutboundMetadata {
    /**
     * The method of the HTTP request.
     */
    @NonNull
    private final String httpMethod;

    /**
     * The path of the URL of the HTTP request.
     */
    @NonNull
    private final String path;

    /**
     * The headers of the HTTP request.
     */
    @NonNull
    private final Map<String, String> headers;
}
