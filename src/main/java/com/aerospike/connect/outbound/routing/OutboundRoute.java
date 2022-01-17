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

package com.aerospike.connect.outbound.routing;

import com.aerospike.connect.outbound.pubsub.PubSubOutboundRoute;
import lombok.NonNull;

import javax.annotation.Nullable;

/**
 * The route to the outbound destination.
 *
 * @param <T> the type of the outbound route. Should be a String type for ESP
 *            (Event Stream Processing), Google Pub/Sub, JMS, Kafka, Pulsar
 *            routes.
 */
public interface OutboundRoute<T> {
    /**
     * Get the type of the outbound route.
     *
     * @return the outbound route type.
     */
    @NonNull
    OutboundRouteType getRouteType();

    /**
     * Get the outbound route.
     *
     * @return the outbound route.
     */
    @NonNull
    T getRoute();

    /**
     * Create an outbound route to an ESP (Event Stream Processing)
     * destination.
     *
     * @param destination the destination name configured in ESP config.
     * @return the outbound route for an ESP destination.
     */
    static DefaultOutboundRoute<String> newEspRoute(String destination) {
        return new DefaultOutboundRoute<>(OutboundRouteType.OTHER, destination);
    }

    /**
     * Create an outbound route to a JMS destination.
     *
     * @param type        type of the JMS destination.
     * @param destination the JMS destination name.
     * @return the outbound route for a JMS destination.
     */
    static DefaultOutboundRoute<String> newJmsRoute(OutboundRouteType type,
                                                    String destination) {
        return new DefaultOutboundRoute<>(type, destination);
    }

    /**
     * Create an outbound route to a Kafka topic.
     *
     * @param topic the Kafka topic name.
     * @return the outbound route for a Kafka topic.
     */
    static DefaultOutboundRoute<String> newKafkaRoute(String topic) {
        return new DefaultOutboundRoute<>(OutboundRouteType.TOPIC, topic);
    }

    /**
     * Create an outbound route to a Pulsar topic.
     *
     * @param topic the Pulsar topic name.
     * @return the outbound route for a Pulsar topic.
     */
    static DefaultOutboundRoute<String> newPulsarRoute(String topic) {
        return new DefaultOutboundRoute<>(OutboundRouteType.TOPIC, topic);
    }

    /**
     * Create an outbound route to a Google Pub/Sub destination.
     *
     * @param topic            the Google Pub/Sub topic name.
     * @param regionalEndpoint the regional endpoint to publish the message to.
     * @return the outbound route for a Google Pub/Sub topic.
     */
    static DefaultOutboundRoute<PubSubOutboundRoute> newPubSubRoute(
            String topic, @Nullable String regionalEndpoint) {
        return new DefaultOutboundRoute<>(OutboundRouteType.TOPIC,
                new PubSubOutboundRoute(topic, regionalEndpoint));
    }
}
