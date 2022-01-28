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

package com.aerospike.connect.outbound.transformer;

import com.aerospike.client.Key;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.connect.outbound.AerospikeOperation;
import com.aerospike.connect.outbound.ChangeNotificationMetadata;
import com.aerospike.connect.outbound.ChangeNotificationRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;

/**
 * {@link Transformer}s should return instances of this class to skip
 * dispatching the change notification record to the outbound destination. The
 * change notification record is acknowledged as successfully handled to
 * Aerospike XDR change notification.
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public class SkipChangeNotificationRecord extends ChangeNotificationRecord {
    public SkipChangeNotificationRecord() {
        super(new ChangeNotificationMetadata(new Key("", "", ""),
                        AerospikeOperation.WRITE, null, null, null,
                        RecordExistsAction.UPDATE, GenerationPolicy.NONE),
                new HashMap<>());
    }
}
