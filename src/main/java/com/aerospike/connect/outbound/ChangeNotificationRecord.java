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

package com.aerospike.connect.outbound;

import com.aerospike.client.Key;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.routing.Router;
import com.aerospike.connect.outbound.transform.Transformer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A notification record shipped by Aerospike change notification when records
 * are inserted, modified or deleted in an Aerospike database.
 */
@EqualsAndHashCode
@Getter
@ToString
public class ChangeNotificationRecord {
    /**
     * Metadata of the changed record.
     */
    @NonNull
    private final ChangeNotificationMetadata metadata;

    /**
     * Map of bin name to bin value of the record. Will be empty in case of a
     * delete operation.
     */
    @NonNull
    private final Map<String, Object> bins;

    /**
     * An unmodifiable map of bin name to bin value of the record. Will be empty
     * in case of a delete operation.
     *
     * <p>
     * When passed as input to the {@link Formatter}, {@link Router}, or {@link
     * Transformer} the blob and GeoJSON values in top level bins, in maps and
     * lists are formatted as mentioned below
     * <ul>
     *     <li>Java blob, C# blob, Python blob, Ruby blob, PHP blob, Erlang
     *     blob and HLL types; are formatted as
     *     {@link com.aerospike.client.Value.BytesValue BytesValue}. The
     *     BytesValue stores the underlying byte array and the
     *     {@link ParticleType}. The
     *     consuming code can choose to interpret the byte array based on the
     *     type of the value: for example the consuming code can choose to
     *     deserialize the Java blob.
     *     </li>
     *     <li>the GeoJSON types are formatted as
     *     {@link com.aerospike.client.Value.GeoJSONValue GeoJSONValue.}
     *     </li>
     *     <li>plain Byte arrays (<code>byte[]</code>) are formatted as
     *     {@link com.aerospike.client.Value.BytesValue BytesValue} with
     *     type as {@link ParticleType#BLOB Blob}.</li>
     * </ul>
     *
     * <p>
     * For details on the bin values to be returned by transformers see
     * {@link Transformer#transform}.
     *
     * @return an unmodifiable map of bins.
     */
    public Map<String, Object> getBins() {
        return Collections.unmodifiableMap(bins);
    }

    /**
     * Helper method for {@link ChangeNotificationMetadata#getKey()
     * getMetadata().getKey()}
     *
     * @return the key of the change notification record.
     * @see ChangeNotificationMetadata#getKey()
     */
    @NonNull
    public Key getKey() {
        return metadata.getKey();
    }

    /**
     * Helper method for {@link ChangeNotificationMetadata#getOperation()
     * getMetadata().getOpertaion()}
     *
     * @return the operation of the change notification record.
     * @see ChangeNotificationMetadata#getOperation()
     */
    @NonNull
    public AerospikeOperation getOperation() {
        return metadata.getOperation();
    }

    /**
     * Helper method for {@link ChangeNotificationMetadata#getGeneration()
     * getMetadata().getGeneration()}
     *
     * @return the generation of the change notification record.
     * @see ChangeNotificationMetadata#getGeneration()
     */
    public Optional<Integer> getGeneration() {
        return metadata.getGeneration();
    }

    /**
     * Helper method for {@link ChangeNotificationMetadata#getLastUpdateTimeMs()
     * getMetadata().getLastUpdateTimeMs()}
     *
     * @return the last update time of the change notification record.
     * @see ChangeNotificationMetadata#getLastUpdateTimeMs()
     */
    public Optional<Long> getLastUpdateTimeMs() {
        return metadata.getLastUpdateTimeMs();
    }

    /**
     * Helper method for {@link ChangeNotificationMetadata#getExpiryTime()
     * getMetadata().getExpiryTime()}
     *
     * @return the expiry time of the change notification record.
     * @see ChangeNotificationMetadata#getExpiryTime()
     */
    public Optional<Integer> getExpiryTime() {
        return metadata.getExpiryTime();
    }

    /**
     * Helper method for {@link ChangeNotificationMetadata#getTimeToLive()
     * getMetadata().getTimeToLive()}
     *
     * @return the time-to-live (seconds from now) of the change notification
     * record.
     * @see ChangeNotificationMetadata#getTimeToLive()
     */
    public Optional<Integer> getTimeToLive() {
        return metadata.getTimeToLive();
    }

    public ChangeNotificationRecord(
            @NonNull ChangeNotificationMetadata metadata,
            @NonNull Map<String, Object> bins) {
        this.metadata = metadata;
        this.bins = new HashMap<>(bins);
    }
}
