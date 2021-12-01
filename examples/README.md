An example project with outbound message transforms for ESP (Event Stream
Processing), Pulsar, etc outbound connectors. The project can be built with
Maven or Gradle.

## Build

All dependencies are shaded to avoid class path conflicts with the outbound
connectors.

### Gradle Build

Run task `shadowJar`. This task builds the JAR at
`build/libs/connect-outbound-transform-examples-all.jar`

```shell
./gradlew shadowJar
```

### Maven Build

Run maven phase `package`. This phase builds the JAR at
`target/connect-outbound-transform-examples-1.0-SNAPSHOT.jar`.

```shell
mvn package
```
