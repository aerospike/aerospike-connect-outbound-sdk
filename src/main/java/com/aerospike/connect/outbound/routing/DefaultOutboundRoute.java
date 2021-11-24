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

import com.aerospike.connect.outbound.routing.OutboundRoute;
import com.aerospike.connect.outbound.routing.OutboundRouteType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * A default implementation of {@link OutboundRoute}.
 *
 * @param <T> the type of the outbound route. Should be a String type for ESP
 *            (Event Stream Processing), Google Pub/Sub, JMS, Kafka, Pulsar
 *            routes.
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DefaultOutboundRoute<T> implements OutboundRoute<T> {
    /**
     * The type of the outbound route.
     */
    @NonNull
    private final OutboundRouteType type;

    /**
     * The outbound route.
     */
    @NonNull
    private final T route;


    @NonNull
    @Override
    public OutboundRouteType getRouteType() {
        return type;
    }

    @NonNull
    @Override
    public T getRoute() {
        return route;
    }
}
