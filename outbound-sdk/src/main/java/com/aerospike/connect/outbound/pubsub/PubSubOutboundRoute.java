/*
 *
 *  Copyright 2012-2025 Aerospike, Inc.
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

package com.aerospike.connect.outbound.pubsub;

import com.aerospike.connect.outbound.routing.DefaultOutboundRoute;
import com.aerospike.connect.outbound.routing.OutboundRouteType;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Optional;

/**
 * The route to a Google Pub/Sub destination.
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class PubSubOutboundRoute extends DefaultOutboundRoute<String> {
    /**
     * The regional endpoint to publish the Google Pub/Sub message to. Ignored
     * for Google Pub/Sub Lite destinations.
     */
    @Nullable
    private final String regionalEndpoint;

    public PubSubOutboundRoute(@NonNull String topic,
                               @Nullable String regionalEndpoint) {
        super(OutboundRouteType.TOPIC, topic);
        this.regionalEndpoint = regionalEndpoint;
    }

    /**
     * The Google Pub/Sub topic name. Route returned by {@link #getRoute()} is a
     * Pub/Sub topic.
     *
     * @return {@link #getRoute()}
     */
    public String getTopic() {
        return getRoute();
    }

    /**
     * Get the regional endpoint to publish the Google Pub/Sub message to.
     * Ignored for Google Pub/Sub Lite destinations.
     *
     * @return the regional endpoint to publish the message.
     */
    public Optional<String> getRegionalEndpoint() {
        return Optional.ofNullable(regionalEndpoint);
    }
}
