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

package com.aerospike.connect.outbound.transform.examples.esp;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.esp.EspOutboundMetadata;
import com.aerospike.connect.outbound.format.DefaultTextOutboundRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;

/**
 * Format incoming change notification record as key value pairs.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transform.examples.esp.EspFormatter
 * </pre>
 */
@Singleton
public class EspFormatter implements Formatter<EspOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(EspFormatter.class.getName());

    @Override
    public OutboundRecord<EspOutboundMetadata> format(
            @NonNull ChangeNotificationRecord record,
            @NonNull Map<String, Object> params,
            @NonNull OutboundRecord<EspOutboundMetadata> formattedRecord) {
        logger.debug("Formatting record {}", record.getKey());

        // Only write string bins.
        StringBuilder payloadBuilder = new StringBuilder();
        for (Map.Entry<String, Object> bin : record.getBins().entrySet()) {
            if (bin.getValue() instanceof String) {
                payloadBuilder.append(bin.getKey());
                payloadBuilder.append(":");
                payloadBuilder.append(bin.getValue());
                payloadBuilder.append("\n");
            }
        }

        return new DefaultTextOutboundRecord<EspOutboundMetadata>(
                payloadBuilder.toString().getBytes(), MediaType.OCTET_STREAM,
                formattedRecord.getMetadata());
    }
}
