package com.aerospike.connect.outbound.transform.examples.esp;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.transform.SkipChangeNotificationRecord;
import com.aerospike.connect.outbound.transform.Transformer;
import com.aerospike.connect.outbound.transform.TransformerConfig;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

/**
 * EspSkipTransformer skips records exceeding a configured generation number.
 *
 * <p>
 * A snippet of a config for this transformer can be
 * <pre>
 * custom-transform:
 *   class: com.aerospike.connect.outbound.transform.examples.esp.EspSkipTransformer
 *   params:
 *     genNumber: 100
 * </pre>
 */
public class EspSkipTransformer implements Transformer {
    private final static Logger logger =
            LoggerFactory.getLogger(EspSkipTransformer.class.getName());

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    @Inject
    public EspSkipTransformer(TransformerConfig routerConfig) {
        this.configParams = routerConfig.getParams();
    }

    @Override
    public ChangeNotificationRecord transform(
            @NonNull ChangeNotificationRecord record) throws Exception {
        // Record generation is not shipped by Aerospike XDR versions before
        // v5.0.0.
        Optional<Integer> generation = record.getGeneration();

        // "genNumber" is to be set in params option of the ESP transformer
        // config.
        if (generation.isPresent() &&
                generation.get() > (int) configParams.get("genNumber")) {
            logger.debug("Skipping record {}", record.getKey());
            return new SkipChangeNotificationRecord();
        }

        return record;
    }
}
