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

package com.aerospike.connect.outbound.elasticsearch.config;

import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.VersionType;
import co.elastic.clients.elasticsearch._types.WaitForActiveShards;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.util.TaggedUnion;
import com.aerospike.connect.outbound.config.DynamicFieldSource;
import com.aerospike.connect.outbound.format.BatchFormatter;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Config parameters for Elasticsearch
 * {@link co.elastic.clients.elasticsearch.core.BulkRequest}.
 */
@Builder
@Jacksonized
@Value
public class BulkRequestConfig {
    /**
     * A constant for injecting {@link BulkRequestConfig} into custom
     * {@link BatchFormatter} implementation.
     */
    public static final String BULK_REQUEST_CONFIG_NAME = "bulk-request-config";

    /**
     * See {@link BulkRequest#pipeline()}.
     *
     * @param pipeline The pipeline id to preprocess incoming documents with.
     * @return the pipeline id to preprocess incoming documents with.
     */
    @Nullable
    String pipeline;

    /**
     * See {@link BulkRequest#refresh()}.
     *
     * @param refresh Whether to refresh the affected shards to make this
     * operation visible to search.
     * @return whether to refresh the affected shards to make this operation
     * visible to search.
     */
    @Nullable
    Refresh refresh;

    /**
     * See {@link BulkRequest#requireAlias()}.
     *
     * @param requireAlias A require_alias field for all incoming documents.
     * @return a require_alias field for all incoming documents.
     */
    @JsonProperty("require-alias")
    @Nullable
    Boolean requireAlias;

    /**
     * See {@link BulkRequest#routing()}.
     *
     * @param routing Specific shard routing value.
     * @return specific shard routing value.
     */
    @Nullable
    @JsonAlias("shard-routing")
    DynamicFieldSource routing;

    /**
     * Explicit operation timeout.
     * <table>
     *   <caption>Supported timeout units</caption>
     *   <tr>
     *      <th>Unit</th>
     *      <th>Example</th>
     *   </tr>
     *   <tr>
     *       <td>m - Minutes</td>
     *       <td>1m</td>
     *   </tr>
     *   <tr>
     *       <td>s - Seconds</td>
     *       <td>10s</td>
     *   </tr>
     *   <tr>
     *       <td>ms - Milliseconds</td>
     *       <td>200ms</td>
     *   </tr>
     *   <tr>
     *       <td>micros - Microseconds</td>
     *       <td>10000micros</td>
     *   </tr>
     *   <tr>
     *       <td>nanos - Nanoseconds</td>
     *       <td>100000nanos</td>
     *   </tr>
     * </table>
     *
     * @param timeout Explicit operation timeout.
     * @return explicit operation timeout.
     */
    @Nullable
    String timeout;

    /**
     * See {@link BulkRequest#waitForActiveShards()}.
     *
     * @param waitForActiveShards The number of shard copies that must be active
     * before proceeding with the bulk operation.
     * @return the number of shard copies that must be active before proceeding
     * with the bulk operation.
     */
    @JsonProperty("wait-for-active-shards")
    @Nullable
    WaitForActiveShards waitForActiveShards;

    /**
     * Mapping of an Aerospike XDR's write operation to Elasticsearch operation.
     * {@link IndexOperationConfig} is the default value.
     *
     * @param aerospikeWriteOperationMapping The {@link OperationConfig}
     * config.
     * @return The {@link OperationConfig} config.
     */
    @Builder.Default
    @JsonProperty("aerospike-write-operation-mapping")
    @NonNull
    OperationConfig aerospikeWriteOperationMapping = DEFAULT_WRITE_OPERATION;

    /**
     * Only perform the operation if the document has this primary term.
     *
     * @param ifPrimaryTerm The primary term.
     * @return The primary term.DEFAULT_WRITE_OPERATION_CONFIG
     */
    @JsonProperty("if-primary-term")
    @Nullable
    Long ifPrimaryTerm;

    /**
     * Only perform the operation if the document has this sequence number.
     *
     * @param ifSeqNo The sequence number.
     * @return The sequence number.
     */
    @JsonProperty("if-seq-no")
    @Nullable
    Long ifSeqNo;

    /**
     * Explicit version number for concurrency control. The specified version
     * must match the current version of the document for the request to
     * succeed.
     *
     * @param version The current document version.
     * @return The current document version.
     */
    @Nullable
    Long version;

    /**
     * Specific version type.
     *
     * @param versionType The specific {@link VersionType}.
     * @return The specific {@link VersionType}.
     */
    @JsonProperty("version-type")
    @Nullable
    VersionType versionType;

    /**
     * Do not process Aerospike's delete record to avoid document deletion from
     * Elasticsearch. It defaults to false.
     *
     * @param ignoreAerospikeDelete Whether to process Aerospike's delete
     * record.
     * @return true if Aerospike's delete should be processed.
     */
    @Builder.Default
    @JsonProperty("ignore-aerospike-delete")
    @NonNull
    Boolean ignoreAerospikeDelete = false;

    /**
     * The default {@link OperationConfig} for XDR's write operation.
     */
    private static final WriteOperationConfig DEFAULT_WRITE_OPERATION =
            new IndexOperationConfig(null);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BulkRequestConfig that = (BulkRequestConfig) o;
        return Objects.equals(pipeline, that.pipeline) &&
                refresh == that.refresh &&
                Objects.equals(requireAlias, that.requireAlias) &&
                Objects.equals(routing, that.routing) &&
                Objects.equals(timeout, that.timeout) &&
                taggedUnionEquals(waitForActiveShards,
                        that.waitForActiveShards) &&
                aerospikeWriteOperationMapping.equals(
                        that.aerospikeWriteOperationMapping) &&
                Objects.equals(ifPrimaryTerm, that.ifPrimaryTerm) &&
                Objects.equals(ifSeqNo, that.ifSeqNo) &&
                Objects.equals(version, that.version) &&
                versionType == that.versionType &&
                ignoreAerospikeDelete == that.ignoreAerospikeDelete;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pipeline, refresh, requireAlias, routing,
                timeout, waitForActiveShards, aerospikeWriteOperationMapping,
                ifPrimaryTerm, ifSeqNo, version, versionType,
                ignoreAerospikeDelete);
    }

    @SuppressWarnings("rawtypes")
    private boolean taggedUnionEquals(TaggedUnion var0, TaggedUnion var1) {
        return var0 == var1 ||
                var0 != null && var0._kind().equals(var1._kind()) &&
                        var0._get().equals(var1._get());
    }
}
