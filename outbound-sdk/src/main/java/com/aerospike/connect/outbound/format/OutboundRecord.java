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

import lombok.NonNull;

import java.util.Collections;
import java.util.Set;

/**
 * OutboundRecord represents the data sent to the outbound destination.
 *
 * <p>
 * OutboundRecord are the data types returned by the {@link Formatter
 * Formatters} for the outbound destinations. Implementing classes should
 * implement one of the derived interfaces -
 * <ul>
 *     <li>{@link BytesOutboundRecord} for all the outbound destinations.</li>
 *     <li>{@link TextOutboundRecord} for JMS outbound destination when
 *     the JMS message should be sent as a JMS TextMessage.</li>
 * </ul>
 *
 * @param <T> the type of metadata associated with the outbound record.
 */
public interface OutboundRecord<T extends OutboundMetadata> {
    /**
     * Get the media type of the outbound record payload.
     *
     * @return the media type of the outbound record payload.
     */
    @NonNull
    String getMediaType();

    /**
     * Get the metadata of the outbound record.
     *
     * @return the metadata of the outbound record.
     */
    @NonNull
    T getMetadata();

    /**
     * External system's errors to be ignored. Failed operation will be
     * considered successful if the result contains the specified error.
     * Depending on a connector, the function can return a set of error codes to
     * ignore, or it can be a set of {@link Exception} class names to ignore
     * etc.
     *
     * @return set of errors to ignore.
     */
    default Set<Object> getIgnoreErrors() {
        return Collections.emptySet();
    }
}
