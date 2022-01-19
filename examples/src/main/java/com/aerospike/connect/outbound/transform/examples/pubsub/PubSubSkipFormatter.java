package com.aerospike.connect.outbound.transform.examples.pubsub;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.format.Formatter;
import com.aerospike.connect.outbound.format.FormatterConfig;
import com.aerospike.connect.outbound.format.MediaType;
import com.aerospike.connect.outbound.format.OutboundRecord;
import com.aerospike.connect.outbound.format.SkipOutboundRecord;
import com.aerospike.connect.outbound.pubsub.PubSubOutboundMetadata;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

/**
 * PubSubSkipFormatter skips record exceeding a configured generation number.
 *
 * <p>
 * A snippet of a config for this formatter can be
 * <pre>
 * format:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transform.examples.pubsub.PubSubSkipFormatter
 *   params:
 *     genNumber: 100
 *   payload-format:
 *     mode: json # Format record with built-in JSON format.
 * </pre>
 */
@Singleton
public class PubSubSkipFormatter implements Formatter<PubSubOutboundMetadata> {
    private final static Logger logger =
            LoggerFactory.getLogger(PubSubSkipFormatter.class.getName());

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    @Inject
    public PubSubSkipFormatter(FormatterConfig routerConfig) {
        this.configParams = routerConfig.getParams();
    }

    @Override
    public OutboundRecord<PubSubOutboundMetadata> format(
            @NonNull ChangeNotificationRecord record,
            @NonNull OutboundRecord<PubSubOutboundMetadata> formattedRecord) {
        logger.debug("Formatting record {}", record.getKey());

        // Record generation is not shipped by Aerospike XDR versions before
        // v5.0.0.
        Optional<Integer> generation = record.getGeneration();

        // "genNumber" is to be set in params option of the PubSub formatter
        // config.
        if (generation.isPresent() &&
                generation.get() > (int) configParams.get("genNumber")) {
            logger.debug("Skipping record {}", record.getKey());
            return new SkipOutboundRecord<>(MediaType.OCTET_STREAM,
                    formattedRecord.getMetadata());
        }

        // Return built-in JSON formatted record.
        return formattedRecord;
    }
}
