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

/**
 * The operation on the Aerospike record. Aerospike change notification ships
 * records on one of these operations on the record.
 */
public enum AerospikeOperation {
    /**
     * A write operation on the record.
     */
    WRITE,

    /**
     * A delete operation on the record.
     */
    DELETE,

    /**
     * A durable delete on the record.
     */
    DURABLE_DELETE;

    /**
     * @return true iff the operation is a delete operation or a durable delete.
     */
    public Boolean isDelete() {
        return this == DELETE || this == DURABLE_DELETE;
    }
}
