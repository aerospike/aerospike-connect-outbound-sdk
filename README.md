# Aerospike Connect Outbound SDK

An outbound SDK for creating custom transforms for streaming connectors.

This package provides a plugin system in Aerospike outbound connectors to
transform records going to the outbound destination with custom code. There are
three transforms that can be selectively configured in the outbound
connectors to apply to the incoming Aerospike change notification records
dispatched by XDR
- routing transform to route the record to the outbound destination.
- record transform to transform the contents of the Aerospike record
  like add bin, delete bin, modify bin value, modify key, modify metadata
  (generation, last update time, expiry).
- format transform to transform the Aerospike record into a custom
  format written to the outbound destination.

The transforms can be plugged in selectively at any of the specificity
levels - at the default level, at the namespace level, at the set level; like the
rest of the configs in the outbound connectors.

The transformers are plugged in by specifying the Java class path of the
custom transform class in the outbound config YAML file. An optional params
map can be specified which will be passed to the transforming method of the
custom transform class.


## Usage

Add a dependency to com.aerospike:aerospike-connect-outbound-sdk.

### Maven

```xml
<dependency>
    <groupId>com.aerospike</groupId>
    <artifactId>aerospike-connect-outbound-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

#### Kotlin DSL

```kotlin
dependencies {
    api("com.aerospike:aerospike-connect-outbound-sdk:1.0.0")
}
```

#### Groovy

```groovy
dependencies {
    api "com.aerospike:aerospike-connect-outbound-sdk:1.0.0"
}
