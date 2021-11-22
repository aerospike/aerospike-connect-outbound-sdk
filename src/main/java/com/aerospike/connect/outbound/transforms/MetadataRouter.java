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

package com.aerospike.connect.outbound.transforms;

/**
 * Route an Aerospike record to an outbound destination.
 *
 * <p>
 * Routers should implement this interface when they require only Aerospike
 * record key and metadata to make routing decisions. If the router requires
 * Aerospike record bins as well to make routing decisions they should implement
 * {@link RecordRouter}.
 * </p>
 *
 * <p>
 * Parsing only the Aerospike record key and metadata is faster than parsing the
 * whole Aerospike record key, metadata and bins. Implementers should choose to
 * implement this interface if bins are not required for making routing
 * decisions.
 * </p>
 *
 * @param <T> the type of the outbound route. Should be a String type for ESP
 *            (Event Stream Processing), Google Pub/Sub, JMS, Kafka, Pulsar
 *            routes.
 */
public interface MetadataRouter<T>
        extends Router<ChangeNotificationMetadata, T> {
}
