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

package com.aerospike.connect.outbound.transformer.examples.esp;


import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.esp.EspOutboundMetadata;
import com.aerospike.connect.outbound.esp.HttpMethod;
import com.aerospike.connect.outbound.format.DefaultBytesOutboundRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.FormatterConfig;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * EspElasticsearchFormatter formats change notification events as Elasticsearch
 * delete or write document requests.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * ...
 * destinations:
 *   dc1:
 *     urls:
 *       - <a href="http://elastic.internal.com:9200">http://elastic.internal.com:9200</a>
 *     protocol: "HTTP_1_1"
 *
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.esp.EspElasticsearchFormatter
 *   params:
 *     username: elastic
 *     password: elastic123
 * </pre>
 */
@Singleton
class EspElasticsearchFormatter implements Formatter<EspOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(EspElasticsearchFormatter.class.getName());
    private static final String ELASTICSEARCH_INDEX_NAME = "esp";

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public EspElasticsearchFormatter(FormatterConfig formatterConfig) {
        configParams = formatterConfig.getParams();
    }

    @Override
    public OutboundRecord<EspOutboundMetadata> format(
            @NonNull ChangeNotificationRecord record,
            @NonNull OutboundRecord<EspOutboundMetadata> formattedRecord)
            throws UnsupportedEncodingException, JsonProcessingException {
        // ESP config should be passed the Elasticsearch username, password for
        // BasicAuth.
        Map<String, String> httpHeaders =
                toBasicAuthHeader((String) configParams.get("username"),
                        (String) configParams.get("password"));

        // Format bins as JSON.
        byte[] jsonFormat = toJsonFormat(record.getBins());

        // Delete a document.
        // See https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-delete.html
        if (record.getMetadata().getOperation().isDelete()) {
            logger.debug("Deleting document for record {}",
                    record.getMetadata().getKey());
            String path =
                    toElasticsearchPath(record.getMetadata().getKey().digest,
                            "_doc");
            EspOutboundMetadata metadata =
                    EspOutboundMetadata.builder(HttpMethod.DELETE)
                            .setPath(path)
                            .setHeaders(httpHeaders)
                            .build();
            return new DefaultBytesOutboundRecord<>(jsonFormat, MediaType.JSON,
                    metadata, Collections.emptySet());
        }

        // Insert/Update a document.
        // See https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-index_.html
        logger.debug("Inserting/updating document for record {}",
                record.getMetadata().getKey());
        String path = toElasticsearchPath(record.getMetadata().getKey().digest,
                "_doc");
        EspOutboundMetadata metadata =
                EspOutboundMetadata.builder(HttpMethod.PUT)
                        .setPath(path)
                        .setHeaders(httpHeaders)
                        .build();
        return new DefaultBytesOutboundRecord<>(jsonFormat, MediaType.JSON,
                metadata, Collections.emptySet());
    }


    private String toDocId(byte[] aerospikeKeyDigest)
            throws UnsupportedEncodingException {
        return URLEncoder.encode(
                Base64.getEncoder().encodeToString(aerospikeKeyDigest),
                "UTF-8");
    }

    private String toElasticsearchPath(byte[] aerospikeKeyDigest, String api)
            throws UnsupportedEncodingException {
        String docId = toDocId(aerospikeKeyDigest);
        return ELASTICSEARCH_INDEX_NAME + "/" + api + "/" + docId;
    }

    private byte[] toJsonFormat(Map<String, Object> bins)
            throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(bins);
    }

    private Map<String, String> toBasicAuthHeader(String username,
                                                  String password) {
        byte[] usernamePassword =
                (username + ":" + password).getBytes(StandardCharsets.UTF_8);
        String encoded = Base64.getEncoder().encodeToString(usernamePassword);

        Map<String, String> authHeader = new HashMap<>();
        authHeader.put("Authorization", "Basic " + encoded);
        return authHeader;
    }
}

