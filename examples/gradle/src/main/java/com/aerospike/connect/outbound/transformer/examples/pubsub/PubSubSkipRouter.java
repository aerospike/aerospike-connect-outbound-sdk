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

package com.aerospike.connect.outbound.transformer.examples.pubsub;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.pubsub.PubSubOutboundRoute;
import com.aerospike.connect.outbound.routing.DefaultOutboundRoute;
import com.aerospike.connect.outbound.routing.OutboundRoute;
import com.aerospike.connect.outbound.routing.OutboundRouteType;
import com.aerospike.connect.outbound.routing.Router;
import com.aerospike.connect.outbound.routing.RouterConfig;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;


/**
 * PubSubSkipRouter skips records exceeding a configured generation number.
 *
 * <p>
 * A snippet of a config for this router can be
 * <pre>
 * routing:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.pubsub.PubSubSkipRouter
 *   params:
 *     genNumber: 100
 * </pre>
 */
public class PubSubSkipRouter implements Router<PubSubOutboundRoute> {
    private final static Logger logger =
            LoggerFactory.getLogger(PubSubSkipRouter.class.getName());

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    @Inject
    public PubSubSkipRouter(RouterConfig routerConfig) {
        configParams = routerConfig.getParams();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public OutboundRoute<PubSubOutboundRoute> getRoute(
            @NonNull ChangeNotificationRecord record) {
        // Record generation is not shipped by Aerospike XDR versions before
        // v5.0.0.
        Optional<Integer> generation = record.getMetadata().getGeneration();

        // "genNumber" is to be set in params option of the PubSub routing
        // config.
        if (generation.isPresent() &&
                generation.get() > (int) configParams.get("genNumber")) {
            logger.debug("Skipping record {}", record.getMetadata().getKey());
            return new DefaultOutboundRoute<>(
                    OutboundRouteType.SKIP, new PubSubOutboundRoute("", null));
        }


        // Destinations default is to be configured in the "destinations"
        // section of the PubSub config.
        logger.debug("Routing record {} to default",
                record.getMetadata().getKey());
        return (OutboundRoute) new PubSubOutboundRoute("default", null);
    }
}
