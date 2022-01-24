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

/**
 * This package provides a plugin system in Aerospike outbound connectors to
 * transform records going to the outbound destination with custom code. Custom
 * code can be plugged in to transform the outbound destination route and the
 * outbound destination record.
 *
 * <p>
 * The transforms can be plugged in selectively at any of the specificity levels
 * - at the default level, at the namespace level, at the set level; like the
 * rest of the configs in the outbound connectors.
 *
 * <p>
 * There are three transforms that can be selectively configured in the outbound
 * connectors to apply to the incoming Aerospike change notification records
 * dispatched by XDR
 * <ul>
 *     <li>routing transform to route the record to the outbound
 *     destination.</li>
 *     <li>record transform to transform the contents of the Aerospike record
 *     like add bin, delete bin, modify bin value, modify key, modify metadata
 *     (generation, last update time, expiry).</li>
 *     <li>format transform to transform the Aerospike record into a custom
 *     format written to the outbound destination.</li>
 * </ul>
 *
 * <p>
 * The transformers are plugged in by specifying the Java class path of the
 * custom transform class in the outbound config YAML file. An optional params
 * map can be specified which will be passed to the transforming method of the
 * custom transform class.
 *
 * @see com.aerospike.connect.outbound.routing.Router
 * @see com.aerospike.connect.outbound.transformer.Transformer
 * @see com.aerospike.connect.outbound.format.Formatter
 */
package com.aerospike.connect.outbound;
