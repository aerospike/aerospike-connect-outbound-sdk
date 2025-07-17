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

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The Aerospike record bin particle types.
 */
public enum ParticleType {
    /**
     * Indicates an unknown value. Used as return value in {@link
     * ParticleType#fromByte}  and {@link ParticleType#fromInt} to indicate an
     * unknown value type.
     */
    UNKNOWN(-1),
    /**
     * Null type.
     */
    NULL(0),
    /**
     * 64-bit integer type.
     */
    INTEGER(1),
    /**
     * 64-bit float type.
     */
    DOUBLE(2),
    /**
     * String type.
     */
    STRING(3),
    /**
     * Binary blob type (bytes).
     */
    BLOB(4),
    /**
     * Serialized Java object.
     */
    JAVA_BLOB(7),
    /**
     * Serialized C# object.
     */
    CSHARP_BLOB(8),
    /**
     * Serialized Python object.
     */
    PYTHON_BLOB(9),
    /**
     * Serialized Ruby object.
     */
    RUBY_BLOB(10),
    /**
     * Serialized PHP object.
     */
    PHP_BLOB(11),
    /**
     * Serialized Erlang object.
     */
    ERLANG_BLOB(12),
    /**
     * Boolean type. Added in Aerospike server 5.6.
     */
    BOOLEAN(17),
    /**
     * HyperLogLog type (HLL).
     */
    HLL(18),
    /**
     * Map Complex Data Type (CDT).
     */
    MAP(19),

    /**
     * List Complex Data Type (CDT).
     */
    LIST(20),
    /**
     * List Complex Data Type (CDT).
     */
    GEOJSON(23);

    /**
     * The value representing the particle type.
     */
    private final int value;

    ParticleType(int value) {
        this.value = value;
    }

    /**
     * @return the value representing the particle type.
     */
    public int getValue() {
        return value;
    }

    /**
     * Map of Particle type value to Particle type.
     */
    private static final Map<Integer, ParticleType> valueToType =
            Arrays.stream(values()).collect(
                    Collectors.toMap(ParticleType::getValue,
                            Function.identity()));

    /**
     * Get the ParticleType from the value.
     *
     * @param value the value representing the particle type.
     * @return the ParticleType represented by value, {@link
     * ParticleType#UNKNOWN} if an unknown value.
     */
    public static ParticleType fromInt(int value) {
        return valueToType.getOrDefault(value, ParticleType.UNKNOWN);
    }

    /**
     * Get the ParticleType from the value.
     *
     * @param value the value representing the particle type.
     * @return the ParticleType represented by value, {@link
     * ParticleType#UNKNOWN} if an unknown value.
     */
    public static ParticleType fromByte(byte value) {
        return fromInt(value);
    }
}
