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

package com.aerospike.connect.outbound.transformer.examples.esp;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.esp.EspOutboundRoute;
import com.aerospike.connect.outbound.routing.OutboundRoute;
import com.aerospike.connect.outbound.routing.Router;
import jakarta.inject.Singleton;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * EspBinRouter routes change notification records based on bin "region".
 *
 * <p>
 * A snippet of a config for this router can be
 * <pre>
 * ...
 *
 * destinations:
 *   internal:
 *     ...
 *   external:
 *     ...
 *
 * routing:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.esp.EspBinRouter
 * </pre>
 */
@Singleton
public class EspBinRouter implements Router<String> {
    private final static Logger logger =
            LoggerFactory.getLogger(EspBinRouter.class.getName());

    @Override
    public OutboundRoute<String> getRoute(
            @NonNull ChangeNotificationRecord record) {
        Map<String, Object> bins = record.getBins();

        // Destinations internal and external are to be configured in the
        // "destinations" section of the ESP config.

        Object region = bins.get("region");
        if ("internal".equals(region)) {
            logger.debug("Routing record {} to internal",
                    record.getMetadata().getKey());
            return new EspOutboundRoute("internal");
        }

        logger.debug("Routing record {} to external",
                record.getMetadata().getKey());
        return new EspOutboundRoute("external");
    }
}
