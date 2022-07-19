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

package com.aerospike.connect.outbound.format;

/**
 * Some predefined media types.
 */
public class MediaType {
    /**
     * Avro media type.
     */
    public final static String AVRO = "application/avro";

    /**
     * JSON media type.
     */
    public final static String JSON = "application/json";

    /**
     * NDJSON media type.
     */
    public final static String NDJSON = "application/x-ndjson";

    /**
     * MessagePack media type.
     */
    public final static String MESSAGE_PACK = "application/x-msgpack";

    /**
     * Octet stream media type.
     */
    public final static String OCTET_STREAM = "application/octet-stream";
}
