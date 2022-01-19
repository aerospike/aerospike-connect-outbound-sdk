package com.aerospike.connect.outbound.transform.examples.esp;

import com.aerospike.connect.outbound.ChangeNotificationRecord;
import com.aerospike.connect.outbound.routing.DefaultOutboundRoute;
import com.aerospike.connect.outbound.routing.OutboundRoute;
import com.aerospike.connect.outbound.routing.OutboundRouteType;
import com.aerospike.connect.outbound.routing.Router;
import com.aerospike.connect.outbound.routing.RouterConfig;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;


/**
 * EspSkipRouter skips records exceeding a configured generation number.
 *
 * <p>
 * A snippet of a config for this router can be
 * <pre>
 * ...
 *
 * destinations:
 *   default:
 *     ...
 *
 * routing:
 *   mode: custom
 *   class: com.aerospike.connect.outbound.transform.examples.esp.EspSkipRouter
 *   params:
 *     genNumber: 100
 * </pre>
 */
public class EspSkipRouter implements Router<String> {
    private final static Logger logger =
            LoggerFactory.getLogger(EspSkipRouter.class.getName());

    /**
     * Params set in the config.
     */
    private final Map<String, Object> configParams;

    @Inject
    public EspSkipRouter(RouterConfig routerConfig) {
        this.configParams = routerConfig.getParams();
    }

    @Override
    public OutboundRoute<String> getRoute(
            @NonNull ChangeNotificationRecord record) {
        // Record generation is not shipped by Aerospike XDR versions before
        // v5.0.0.
        Optional<Integer> generation = record.getGeneration();

        // "genNumber" is to be set in params option of the ESP routing config.
        if (generation.isPresent() &&
                generation.get() > (int) configParams.get("genNumber")) {
            logger.debug("Skipping record {}", record.getKey());
            return new DefaultOutboundRoute<String>(OutboundRouteType.SKIP, "");
        }


        // Destinations default is to be configured in the "destinations"
        // section of the ESP config.
        logger.debug("Routing record {} to default", record.getKey());
        return OutboundRoute.newEspRoute("default");
    }
}
