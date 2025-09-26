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

package com.aerospike.connect.outbound.transformer.examples.pubsub;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.pubsub.PubSubOutboundRoute;
import com.aerospike.connect.outbound.routing.OutboundRoute;
import com.aerospike.connect.outbound.routing.Router;
import jakarta.inject.Singleton;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * PubSubBinRouter routes change notification records based on bin "region".
 *
 * <p>
 * A snippet of a config for this router can be
 * <pre>
 * routing:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.pubsub.PubSubBinRouter
 * </pre>
 */
@Singleton
public class PubSubBinRouter implements Router<PubSubOutboundRoute> {
    private final static Logger logger =
            LoggerFactory.getLogger(PubSubBinRouter.class.getName());

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public OutboundRoute<PubSubOutboundRoute> getRoute(
            @NonNull ChangeNotificationRecord record) {
        Map<String, Object> bins = record.getBins();

        Object region = bins.get("region");
        if (region instanceof String) {
            logger.debug("Routing record {} to {}",
                    record.getMetadata().getKey(), region);
            return (OutboundRoute) new PubSubOutboundRoute(
                    (String) region, null);
        }

        logger.debug("Routing record {} to default",
                record.getMetadata().getKey());
        return (OutboundRoute) new PubSubOutboundRoute("default", null);
    }
}
