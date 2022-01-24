An example project with outbound message transformers for ESP (Event Stream
Processing), Pulsar, etc. outbound connectors. The project can be built with
Maven or Gradle.

## Build

The plugin should be compiled with the same/compatible Java version running the
outbound connector. And all dependencies should be shaded to avoid class path
conflicts with the outbound connectors.

### Gradle Build

Run task `shadowJar`. This task builds the JAR at
`build/libs/connect-outbound-transformer-examples-1.0.0-SNAPSHOT-all.jar`

```shell
./gradlew shadowJar
```

### Maven Build

Run maven phase `package`. This phase builds the JAR at
`target/connect-outbound-transformer-examples-1.0.0-SNAPSHOT.jar`.

```shell
mvn package
```

## Deploy the custom code

Copy the generated JARs to `/opt/aerospike-<outbound-connector>/usr-lib`
directory before starting the outbound connector.
