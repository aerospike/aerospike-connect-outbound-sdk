# Releasing the SDK

Work lands on `main` (squash-merge pull requests). Each artifact has its own
`gradle.properties` and ships independently:

| Artifact | Version file | Git tag prefix / JFrog bundle |
|----------|--------------|-------------------------------|
| Outbound SDK | `outbound-sdk/gradle.properties` | `aerospike-connect-outbound-sdk` |
| Elasticsearch outbound SDK | `elasticsearch-outbound-sdk/gradle.properties` | `aerospike-connect-elasticsearch-outbound-sdk` |

Bump the module you are changing. The other artifact does not have to ship.

QE certifies `-N` bundles tagged on `main` for that artifact. The shippable
release is `<artifact>-X.Y.Z` at the **same git commit** as the highest `-N`
for that artifact on `main` (commit-id similarity, not a byte-identical
rebuild). If QE rejects a build, leave `main` as-is, land a fix, and tag a new
N. Do not rewrite `main`.

| Kind | Git tag | Bundle name | Bundle / Maven | From | Annotation |
|------|---------|-------------|----------------|------|------------|
| Dev try-out | `aerospike-connect-outbound-sdk-3.0.2-1` | `aerospike-connect-outbound-sdk` | `3.0.2-1` | any branch (stays in DEV) | `3.0.2` |
| QE | `aerospike-connect-outbound-sdk-3.0.2-2` | `aerospike-connect-outbound-sdk` | `3.0.2-2` | `main` | `3.0.2` |
| GA | `aerospike-connect-outbound-sdk-3.0.2` | `aerospike-connect-outbound-sdk` | `3.0.2` | same commit as that artifact's highest `-N` on `main` | `3.0.2` |

Elasticsearch uses the same `X.Y.Z-N` / `X.Y.Z` suffix on its own prefix, and
its own bundle, even when the numeric line differs from the outbound SDK.

A `-N` tag may be pushed from any branch, so a dev can get a DEV bundle without
touching `main`. Pick an unused N yourself
(`git tag aerospike-connect-outbound-sdk-3.0.2-3 && git push origin aerospike-connect-outbound-sdk-3.0.2-3`).
**Promote SDK release bundle** is the gate: it requires that git tag to be
on `main`, so a bundle built off a dev branch cannot go past DEV.

**QE:** squash-merge the work to `main`, tag `<artifact>-X.Y.Z-N` on that
commit, and run **Build SDK release to DEV**. Promote with the TEST/STAGE
checkboxes. If QE fails, merge the fix to `main` and repeat with the next N.

**GA:** after QE accepts, tag `<artifact>-X.Y.Z` on the same SHA as that `-N`
(usually current `main` HEAD). The workflow rejects a GA tag that is not on
`main` or that does not match the highest `-N` already on `main` for that
artifact.

**Build SDK release to DEV** signs and deploys only the tagged artifact,
creates that artifact's JFrog bundle, annotates it with the logical version
from that module's `gradle.properties`, and promotes to DEV. Consume the exact
Maven GAV from the connect DEV Maven repository.

**Promote SDK release bundle** asks for the git tag and which environments to
move. TEST only stops at TEST. STAGE promotes TEST first unless that version is
already on TEST, then promotes STAGE. PROD is the org approval path;
[`citrusleaf/artifact-publisher`](https://github.com/citrusleaf/artifact-publisher)
publishes to Maven Central after PROD, not STAGE.

JFrog bundle versions are immutable. Each QE attempt needs a new `-N`. The GA
version `X.Y.Z` is a separate bundle. The release workflow checks that the tagged
module's `gradle.properties` matches the logical version.

Gradle only generates the JARs, POM, and `.module` file. Signing, deploy, and
promotion are `aerospike/shared-workflows`. The `.module` file is copied next
to the JAR stem so deploy-artifacts picks it up.
