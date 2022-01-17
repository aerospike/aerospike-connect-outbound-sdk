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

package com.aerospike.connect.outbound.pubsub;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * The route to a Google Pub/Sub destination.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class PubSubOutboundRoute {
    /**
     * The Google Pub/Sub topic name.
     */
    private final String topic;

    /**
     * The regional endpoint to publish the Google Pub/Sub message to. Ignored
     * for Google Pub/Sub Lite destinations.
     */
    @Nullable
    private final String regionalEndpoint;


    /**
     * Get the regional endpoint to publish the Google Pub/Sub message to.
     *
     * @return the regional endpoint to publish the message.
     */
    public Optional<String> getRegionalEndpoint() {
        return Optional.ofNullable(regionalEndpoint);
    }
}
