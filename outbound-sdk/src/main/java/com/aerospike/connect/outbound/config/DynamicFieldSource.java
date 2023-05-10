package com.aerospike.connect.outbound.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * Configuration for computing dynamic fields from incoming change notification.
 * Examples of dynamic fields are routes, destination system message/record keys
 * or ids.
 */
@JsonTypeInfo(use = Id.NAME, property = "source", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DynamicFieldSourceNone.class,
                name = "none"),
        @JsonSubTypes.Type(value = DynamicFieldSourceSystemDefault.class,
                name = "system-default"),
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
                name = "static"),
        @JsonSubTypes.Type(value = DynamicFieldSourceKeyConcat.class,
                name = "key-concat"),
        @JsonSubTypes.Type(value = DynamicFieldSourceKeyHash.class,
                name = "key-hash")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface DynamicFieldSource {
    /**
     * Strategy to be used when the value cannot be computed using the specified
     * source.
     *
     * @return the {@link DynamicFieldSourceFailureStrategy}.
     * @throws Exception if the value should always be computable for the given
     *                   {@link DynamicFieldSource}.
     */
    DynamicFieldSourceFailureStrategy getDynamicFieldSourceFailureStrategy()
            throws Exception;

    /**
     * Validate the configuration.
     *
     * @throws Exception if validation fails.
     */
    default void validate() throws Exception {
    }

    /**
     * Specifies whether this {@link DynamicFieldSource} can be used for
     * generating a key of a batch of records.
     *
     * @return a boolean value indicating whether this
     * {@link DynamicFieldSource} can be used for generating a key of a batch of
     * records.
     */
    default boolean isAllowedForBatchKey() {
        return false;
    }
}
