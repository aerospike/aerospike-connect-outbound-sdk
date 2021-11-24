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

package com.aerospike.connect.outbound;

import com.aerospike.client.Key;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Metadata of the change notification record.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class ChangeNotificationMetadata {
    /**
     * Key of the changed record.
     */
    @NonNull
    Key key;

    /**
     * The operation on the record which triggered a ship by Aerospike XDR.
     */
    @NonNull
    private final AerospikeOperation operation;

    /**
     * Record modification count. It is not shipped by Aerospike XDR versions
     * before v5.0.0, and will be <code>null</code> in these cases.
     */
    @Nullable
    private final Integer generation;

    /**
     * Last update time of the record, measured in milliseconds since the Unix
     * epoch. It is not shipped by Aerospike XDR versions before v5.0.0, and
     * will be <code>null</code> in these cases.
     */
    @Nullable
    private final Long lastUpdateTimeMs;

    /**
     * The expiry time of the record, measured in seconds since the Unix epoch.
     * It is shipped by Aerospike XDR only for write operations, and will be
     * <code>null</code> for delete operations. The value will be
     * <code>-1</code> if the record never expires.
     */
    @Nullable
    private final Integer expiryTime;

    /**
     * Get the generation of the record. It is not shipped by Aerospike XDR
     * versions before v5.0.0, and will be <code>null</code> in these cases.
     *
     * @return the generation of the record.
     */
    public Optional<Integer> getGeneration() {
        return Optional.ofNullable(generation);
    }

    /**
     * Get the last update time of the record, measured in milliseconds since
     * the Unix epoch. It is not shipped by Aerospike XDR versions before
     * v5.0.0, and will be <code>null</code> in these cases.
     *
     * @return the last update time of the record.
     */
    public Optional<Long> getLastUpdateTimeMs() {
        return Optional.ofNullable(lastUpdateTimeMs);
    }

    /**
     * Get the expiry time of the record, measured in seconds since the Unix
     * epoch. It is shipped by Aerospike XDR only for write operations, and will
     * be <code>null</code> for delete operations. The value will be
     * <code>-1</code> if the record never expires.
     *
     * @return the expiry time of the record.
     */
    public Optional<Integer> getExpiryTime() {
        return Optional.ofNullable(expiryTime);
    }

    /**
     * Convert record expiration to time-to-live (seconds from now). Since the
     * expiry time is shipped by Aerospike XDR only for write operations,
     * time-to-live will be <code>null</code> for delete operations. The value
     * will be <code>0</code> if the record has already expired and
     * <code>-1</code> if the record never expires.
     *
     * @return the time-to-live (ttl) of the record, measured in seconds from
     * now.
     */
    public Optional<Integer> getTimeToLive() {
        if (expiryTime == null) {
            return Optional.empty();
        }

        // Expiry is measured in seconds since Unix epoch.
        if (expiryTime == 0) { // record never expires
            return Optional.of(-1);
        }

        int now = (int) (System.currentTimeMillis() / 1000);
        if (expiryTime > now) {
            return Optional.of(expiryTime - now);
        } else {
            return Optional.of(0); // already expired
        }
    }
}
