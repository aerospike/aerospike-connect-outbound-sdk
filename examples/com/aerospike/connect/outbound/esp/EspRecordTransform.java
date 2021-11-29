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

package com.aerospike.connect.outbound.transforms.examples.esp;

import com.aerospike.connect.outbound.ChangeNotificationMetadata;
import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.transform.Transformer;
import lombok.NonNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Transform a change notification record.
 */
public class EspRecordTransform implements Transformer {
    @Override
    public ChangeNotificationRecord transform(
            @NonNull ChangeNotificationRecord record,
            @NonNull Map<String, Object> params) {
        // Increment generation in metadata.
        Integer generation = record.getGeneration().isPresent() ?
                record.getGeneration().get() + 1 : null;

        Long lastUpdateTime = record.getLastUpdateTimeMs().isPresent() ?
                record.getLastUpdateTimeMs().get() : null;

        Integer expiryTime = record.getExpiryTime().isPresent() ?
                record.getExpiryTime().get() : null;

        ChangeNotificationMetadata metadata =
                new ChangeNotificationMetadata(record.getKey(),
                        record.getOperation(), generation, lastUpdateTime,
                        expiryTime);

        // Add a bin.
        Map<String, Object> bins = new HashMap<>(record.getBins());
        bins.put("timestamp", (new Date()).getTime());

        return new ChangeNotificationRecord(metadata, bins);
    }
}
