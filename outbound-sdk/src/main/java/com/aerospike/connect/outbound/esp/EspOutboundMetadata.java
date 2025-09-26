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

package com.aerospike.connect.outbound.esp;

import com.aerospike.connect.outbound.format.OutboundMetadata;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The metadata associated with the ESP (Event Stream Processing) outbound
 * record.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class EspOutboundMetadata implements OutboundMetadata {
    /**
     * @return The method of the HTTP request.
     */
    @NonNull
    private final HttpMethod httpMethod;

    /**
     * The set of HTTP status codes indicating a successful response. If it is
     * not specified then the status codes specified in the ESP destination
     * config are used.
     */
    @Nullable
    private final Set<Integer> successStatusCodes;

    /**
     * The path component of the relative URL resolved against the base URL (ESP
     * destination URL) to get the target URL. See {@link
     * java.net.URI#resolve(URI)} and
     * <a href="https://datatracker.ietf.org/doc/html/rfc3986/#section-5.4">examples</a>.
     *
     * <p>
     * Should be in non percent-encoded, plain human-readable form.
     */
    @Nullable
    private final String path;

    /**
     * The query component of the relative URL resolved against the base URL
     * (ESP destination URL) to get the target URL. See {@link
     * java.net.URI#resolve(URI)} and
     * <a href="https://datatracker.ietf.org/doc/html/rfc3986/#section-5.4">examples</a>.
     *
     * <p>
     * Should be in non percent-encoded, plain human-readable form.
     */
    @Nullable
    private final String query;

    /**
     * The headers of the HTTP request.
     */
    @Nullable
    private final Map<String, String> headers;


    /**
     * Get the headers of the HTTP request.
     *
     * @return the HTTP headers.
     */
    public Optional<Map<String, String>> getHeaders() {
        return Optional.ofNullable(headers);
    }

    /**
     * Get the set of HTTP status codes indicating a successful response. If it
     * is not specified then the status codes specified in the ESP destination
     * config are used.
     *
     * @return set of HTTP status codes indicating a successful response.
     */
    public Optional<Set<Integer>> getSuccessStatusCodes() {
        return Optional.ofNullable(successStatusCodes);
    }

    /**
     * Get the path component of the relative URL resolved against the base URL
     * (ESP destination URL) to get the target URL. See {@link
     * java.net.URI#resolve(URI)} and
     * <a href="https://datatracker.ietf.org/doc/html/rfc3986/#section-5.4">examples</a>.
     *
     * <p>
     * Should be in non percent-encoded, plain human-readable form.
     *
     * @return the path component of the relative URL.
     */
    public Optional<String> getPath() {
        return Optional.ofNullable(path);
    }

    /**
     * Get the query component of the relative URL resolved against the base URL
     * (ESP destination URL) to get the target URL. See {@link
     * java.net.URI#resolve(URI)} and
     * <a href="https://datatracker.ietf.org/doc/html/rfc3986/#section-5.4">examples</a>.
     *
     * <p>
     * Should be in non percent-encoded, plain human-readable form.
     *
     * @return the query component of the relative URL.
     */
    public Optional<String> getQuery() {
        return Optional.ofNullable(query);
    }


    /**
     * Create a new EspOutboundMetadata builder.
     *
     * @param httpMethod the HTTP method of the ESP metadata
     * @return a new builder.
     */
    public static Builder builder(@NonNull HttpMethod httpMethod) {
        return new Builder(httpMethod);
    }

    public static class Builder {
        private final HttpMethod httpMethod;
        private String path;
        private String query;
        private Map<String, String> headers;
        private Set<Integer> successStatusCodes;

        public Builder(@NonNull HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
        }

        public Builder setPath(@Nullable String path) {
            this.path = path;
            return this;
        }

        public Builder setQuery(@Nullable String query) {
            this.query = query;
            return this;
        }

        public Builder setHeaders(@Nullable Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setSuccessStatusCodes(
                @Nullable Set<Integer> successStatusCodes) {
            this.successStatusCodes = successStatusCodes;
            return this;
        }

        public EspOutboundMetadata build() {
            return new EspOutboundMetadata(httpMethod, successStatusCodes,
                    path, query, headers);
        }
    }
}
