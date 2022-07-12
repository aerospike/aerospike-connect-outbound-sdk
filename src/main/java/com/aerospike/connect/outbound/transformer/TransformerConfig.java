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

package com.aerospike.connect.outbound.transformer;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Map;

/**
 * The Transformer configuration injected in the constructor of the custom
 * Transformer instance.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class TransformerConfig {
    /**
     * @return The parameters passed to the Transformer in the outbound
     * configuration. <b>WARN:</b> {@code params} is an unmodifiable map. The
     * map and all its values should be treated as immutable.
     */
    @NonNull
    @Getter private final Map<String, Object> params;
}
