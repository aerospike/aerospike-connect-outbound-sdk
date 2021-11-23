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

package com.aerospike.connect.outbound.transforms;

import lombok.NonNull;

import java.util.Map;

/**
 * Transform the Aerospike record like add bin, delete bin, modify bin value,
 * modify key, modify metadata (generation, last update time, expiry).
 *
 * <p>
 * The transformed record is either passed as input to a {@link Formatter} or is
 * processed into one of the inbuilt outbound formats - Avro, MessagePack, etc.
 * </p>
 */
public interface Transformer {
    /**
     * Transform the Aerospike record like add bin, delete bin, modify bin
     * value, modify key, modify metadata (generation, last update time,
     * expiry).
     *
     * <p>
     * The returned {@link ChangeNotificationRecord} bins can contain plain Java
     * objects or/and Aerospike client {@link com.aerospike.client.Value
     * Values}. When the transformed record is used as input to the FlatJSON,
     * JSON, MessagePack inbuilt outbound formats there are differences in their
     * formatting as mentioned below
     * <table>
     *     <tr>
     *     <th>Value</th>
     *     <th>Java Type</th>
     *     <th>Aerospike client {@link com.aerospike.client.Value Value} type</th>
     *     </tr>
     *     <tr>
     *     <td>Geo JSON String</td>
     *     <td>If represented as a Java String it is stored as plain string in
     *     Json, FlatJSON and MessagePack outbound format.</td>
     *     <td>If represented as
     *     {@link com.aerospike.client.Value.GeoJSONValue GeoJSONValue} it is
     *     stored
     *     <ul>
     *     <li>as a JSON object in Json and FlatJSON outbound format</li>
     *     <li>as a bin with GeoJSON type in top level bin and as a
     *     MessagePack ext format type (with ext type equal to GeoJSON) in maps
     *     and lists, in MessagePack outbound format.</li>
     *     </ul>
     *     </td>
     *
     *     <tr>
     *     <td>Java blob, C# blob, Python blob, Ruby blob, PHP blob, Erlang
     *      blob and HLL types</td>
     *      <td>If represented by Java byte array (<code>byte[]</code>) it is
     *      stored
     *      <ul>
     *      <li>as Base64 encoded string in Json and FlatJSON outbound
     *      format</li>
     *      <li>as a Blob type in top level bin and as a binary value in
     *      maps and lists, in MessagePack outbound format.</li>
     *      </ul>
     *      </td>
     *      <td>If represented as a
     *      {@link com.aerospike.client.Value.BytesValue BytesValue} it is
     *      stored
     *      <ul>
     *          <li>as a Base64 encode string in Json and FlatJSON outbound
     *          format
     *          </li>
     *          <li>as a bin with the exact type in top level bin and as a
     *          MessagePack ext format type (with ext type equal to the type)
     *          in maps and lists, in MessagePack outbound format.</li>
     *      </ul></td>
     *     </tr>
     *     </tr>
     * </table>
     * </p>
     *
     * @param record the change notification record.
     * @param params the params passed to the record from the config. Is an
     *               unmodifiable map.
     * @return the transformed change notification record.
     */
    ChangeNotificationRecord transform(@NonNull ChangeNotificationRecord record,
                                       @NonNull Map<String, Object> params) throws Exception;
}
