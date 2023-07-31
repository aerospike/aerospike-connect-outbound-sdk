package com.aerospike.connect.outbound.transformer.examples.jms;

import com.aerospike.connect.outbound.AerospikeOperation;
import com.aerospike.connect.outbound.format.BatchFormatter;
import com.aerospike.connect.outbound.format.BatchItem;
import com.aerospike.connect.outbound.format.DefaultTextOutboundRecord;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.aerospike.connect.outbound.jms.JmsOutboundMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * JmsBatchFormatter formats a batch of change notification records into a
 * single JMS message.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * batching:
 *   enabled: true
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transformer.examples.jms.JmsBatchFormatter
 * </pre>
 */
public class JmsBatchFormatter implements BatchFormatter<JmsOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(JmsBatchFormatter.class.getName());

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<OutboundRecord<JmsOutboundMetadata>> format(
            @NonNull List<BatchItem<JmsOutboundMetadata>> batchItems)
            throws Exception {
        Map<String, Map<String, String>> records = batchItems.stream()
                // Filter only writes.
                .filter(batchItem -> batchItem.getRecord().getMetadata().getOperation() == AerospikeOperation.WRITE)

                // Convert to map of key to map of bins.
                .collect(Collectors.toMap(
                        batchItem -> batchItem.getRecord().getMetadata().getKey().toString(),
                        batchItem -> batchItem.getRecord().getBins().entrySet().stream().collect(
                                Collectors.toMap(Entry::getKey,
                                        bin -> bin.getValue().toString()))
                ));

        logger.debug("Batch size {}", records.size());

        if (records.isEmpty()) {
            return Collections.emptyList();
        }

        // Convert Map of records to JSON.
        byte[] recordsJson = objectMapper.writeValueAsBytes(records);

        // Merge all the Batch items into a single record.
        return Collections.singletonList(new DefaultTextOutboundRecord<>(
                recordsJson, MediaType.OCTET_STREAM,
                new JmsOutboundMetadata(), Collections.emptySet()
        ));
    }
}
