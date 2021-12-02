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

package com.aerospike.connect.outbound.transform.examples.esp;


import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.routing.OutboundRoute;
import com.aerospike.connect.outbound.routing.Router;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;


/**
 * Route records by generation number.
 *
 * <p>
 * A snippet of a config for this router can be
 * <pre>
 * ...
 *
 * destinations:
 *   old:
 *     ...
 *   young:
 *     ...
 *
 * routing:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transform.examples.esp.EspGenerationRouter
 *   params:
 *     genNumber: 100
 * </pre>
 */
@Singleton
public class EspGenerationRouter implements Router<String> {
    private final static Logger logger =
            LoggerFactory.getLogger(EspGenerationRouter.class.getName());

    @Override
    public OutboundRoute<String> getRoute(
            @NonNull ChangeNotificationRecord record,
            @NonNull Map<String, Object> params) {
        // Record generation is not shipped by Aerospike XDR versions before
        // v5.0.0.
        Optional<Integer> generation = record.getGeneration();

        // Destinations young and old are to be configured in the
        // "destinations" section of the ESP config.
        //
        // "genNumber" is to be set in params option of the ESP routing config.

        if (generation.isPresent() &&
                generation.get() > (int) params.get("genNumber")) {
            logger.debug("Routing record {} to old", record.getKey());
            return OutboundRoute.newEspRoute("old");
        }

        logger.debug("Routing record {} to young", record.getKey());
        return OutboundRoute.newEspRoute("young");
    }
}
