# Aerospike Connect Outbound SDK

An outbound SDK for creating custom transforms for streaming connectors.

Aerospike Connect outbound transform provides a plugin system in Aerospike
outbound connectors to transform incoming XDR change notification records
dispatched to the outbound destination with custom code.

Three transforms can be configured in any combination in the outbound connectors
to apply to the incoming Aerospike change notification records dispatched by XDR:

- **Routing transform:** Route the record to the outbound destination.
- **Record transform:** Transform the contents of the Aerospike record. Transformations include:
    - Add bin
    - Delete bin
    - Change bin value
    - Change key
    - Change metadata (generation, last update time, expiry)
- **Format transform:** Transform the Aerospike record into a custom format written to the outbound destination.

The transformers are plugged in by specifying the Java class path of the
custom transform class in the outbound config YAML file. An optional params
map can be specified which will be passed to the transforming method of the
custom transform class.

For detailed documentation see [Aerospike connector docs](https://docs.aerospike.com/docs/connect/streaming-from-asdb/message-transform-outbound.html)

## Usage

Add a dependency to this repo in your project. The plugin should be compiled
with the same/compatible Java version running the outbound connector.

See the build files in the [examples](/examples) folder for further details.

**NOTE** It is recommended to shade all the custom plugin dependencies to avoid
class path conflicts with the outbound connectors.

### Maven

```xml
<dependency>
    <groupId>com.aerospike</groupId>
    <artifactId>aerospike-connect-outbound-sdk</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

### Gradle

#### Kotlin DSL

```kotlin
dependencies {
    compileOnly("com.aerospike:aerospike-connect-outbound-sdk:1.0.0")
}
```

#### Groovy

```groovy
dependencies {
    compileOnly "com.aerospike:aerospike-connect-outbound-sdk:1.0.0"
}
```

## Examples

See [examples](/examples) folder.
