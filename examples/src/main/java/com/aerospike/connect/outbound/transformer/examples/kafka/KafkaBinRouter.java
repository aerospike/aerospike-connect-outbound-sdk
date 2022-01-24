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

package com.aerospike.connect.outbound.transformer.examples.kafka;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.routing.OutboundRoute;
import com.aerospike.connect.outbound.routing.Router;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;

/**
 * KafkaBinRouter routes records based on bin "region".
 *
 * <p>
 * A snippet of a config for this router can be
 * <pre>
 * routing:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.kafka.KafkaBinRouter
 * </pre>
 */
@Singleton
public class KafkaBinRouter implements Router<String> {
    private final static Logger logger =
            LoggerFactory.getLogger(KafkaBinRouter.class.getName());

    @Override
    public OutboundRoute<String> getRoute(
            @NonNull ChangeNotificationRecord record) {
        Map<String, Object> bins = record.getBins();

        Object region = bins.get("region");
        if (region instanceof String) {
            logger.debug("Routing record {} to {}", record.getKey(), region);
            return OutboundRoute.newKafkaRoute((String) region);
        }

        logger.debug("Routing record {} to default", record.getKey());
        return OutboundRoute.newKafkaRoute("default");
    }
}
