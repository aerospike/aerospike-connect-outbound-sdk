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

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import lombok.NonNull;

import java.util.Map;

/**
 * Route records to the outbound destination.
 *
 * @param <T> the type of the outbound route. Should be a String type for ESP
 *            (Event Stream Processing), Google Pub/Sub, JMS, Kafka, Pulsar
 *            routes.
 */
public interface Router<T> {
    /**
     * Get the route for the record.
     *
     * <p>
     * If the returned route has route type {@link OutboundRouteType#SKIP SKIP}
     * then the change notification record is skipped, and not dispatched to the
     * outbound destination.
     *
     * <p>
     * When an exception is thrown by this method, the record is acknowledged
     * with temporary error to Aerospike XDR change notification. Aerospike XDR
     * change notification will resend the change notification record on a
     * temporary error.
     *
     * @param record the change notification record.
     * @param params the params passed to the record from the config. Is an
     *               unmodifiable map.
     * @return the route for the record.
     * @throws Exception if failed to route the record. The record is
     *                   acknowledged with temporary error to Aerospike XDR
     *                   change notification.
     */
    OutboundRoute<T> getRoute(@NonNull ChangeNotificationRecord record,
                              @NonNull Map<String, Object> params)
            throws Exception;
}
