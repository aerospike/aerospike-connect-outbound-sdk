package com.aerospike.connect.outbound.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * Configuration for computing unique key/id for the destination system when
 * batching is used.
 */
@JsonTypeInfo(use = Id.NAME, property = "source", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DynamicFieldSourceAuto.class,
                name = "auto"),
        @JsonSubTypes.Type(value = DynamicFieldSourceNamespace.class,
                name = "namespace"),
        @JsonSubTypes.Type(value = DynamicFieldSourceSet.class,
                name = "set"),
        @JsonSubTypes.Type(value = DynamicFieldSourceDigest.class,
                name = "digest"),
        @JsonSubTypes.Type(value = DynamicFieldSourceUserKey.class,
                name = "user-key"),
        @JsonSubTypes.Type(value = DynamicFieldSourceBinValue.class,
                name = "bin-value"),
        @JsonSubTypes.Type(value = DynamicFieldSourceStatic.class,
                name = "static")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface DynamicFieldSource {
    /**
     * Strategy to be used when we cannot compute the value using the specified
     * source.
     */
    DynamicFieldSourceFailureStrategy getDynamicFieldSourceFailureStrategy()
            throws Exception;

    /**
     * Validate the configuration.
     */
    default void validate() throws Exception {
    }
}