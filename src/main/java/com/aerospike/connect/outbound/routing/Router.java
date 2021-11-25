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

package com.aerospike.connect.outbound.routing;

import lombok.NonNull;

import java.util.Map;

/**
 * Route records to the outbound destination.
 *
 * <p>
 * This is the root interface for all the different outbound routers.
 * Implementers should implement one of the derived interfaces - {@link RecordRouter}.
 * </p>
 *
 * @param <U> the input data type to the router.
 * @param <V> the type of the outbound route. Should be a String type for ESP
 *            (Event Stream Processing), Google Pub/Sub, JMS, Kafka, Pulsar
 *            routes.
 */
public interface Router<U, V> {
    /**
     * Get the route for the record data.
     *
     * @param recordData data of the change notification record.
     * @param params     the config parameters passed to the router. Is an
     *                   unmodifiable map.
     * @return the route for the record.
     * @throws Exception if failed to route the record.
     */
    OutboundRoute<V> getRoute(@NonNull U recordData,
                              @NonNull Map<String, Object> params) throws Exception;
}
