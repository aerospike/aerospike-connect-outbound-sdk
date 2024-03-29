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

package com.aerospike.connect.outbound.transformer.examples.elasticsearch;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Value;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@JsonSerialize(using = PreSerializedJson.Serializer.class)
@Value
public class PreSerializedJson {
    byte[] value;

    public static final class Serializer
            extends StdSerializer<PreSerializedJson> {

        private static final long serialVersionUID = -5630063916123919444L;

        public Serializer() {
            super(PreSerializedJson.class);
        }

        @Override
        public void serialize(PreSerializedJson value, JsonGenerator gen,
                              SerializerProvider provider) throws IOException {
            gen.writeRawValue(new String(value.value, StandardCharsets.UTF_8));
        }
    }
}
