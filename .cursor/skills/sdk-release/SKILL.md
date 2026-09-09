---
name: sdk-release
description: >-
  Tag QE <artifact>-X.Y.Z-N or GA <artifact>-X.Y.Z on main. Use when the
  user asks to cut a release tag after work is squash-merged to main.
  Artifacts: aerospike-connect-outbound-sdk,
  aerospike-connect-elasticsearch-outbound-sdk.
disable-model-invocation: true
---

# Tag SDK release on main

Work is already on `main` (GitHub squash-merge). Do not squash or
force-push `main`. Follow `docs/releasing.md`.

The two artifacts release independently. Require which one. Tag prefix,
JFrog bundle name, and Maven artifactId are the same string.

| Artifact | Version file |
|----------|--------------|
| `aerospike-connect-outbound-sdk` | `outbound-sdk/gradle.properties` |
| `aerospike-connect-elasticsearch-outbound-sdk` | `elasticsearch-outbound-sdk/gradle.properties` |

## QE (only if asked)

1. Refuse unless `git branch --show-current` is `main`, the working tree is
   clean, and `HEAD` matches `origin/main`.
2. `git fetch origin --tags`. Confirm an unused `<artifact>-X.Y.Z-N` for
   the version in that module's `gradle.properties`.
3. Show the tag and SHA. After the user confirms:

```bash
git tag <artifact>-<X.Y.Z-N>
git push origin <artifact>-<X.Y.Z-N>
```

If QE later rejects that bundle, land a fix on `main` and tag the next N
for the same artifact. Do not reset or force-push `main`.

## GA (only if asked)

Tag `<artifact>-X.Y.Z` at the same commit as the highest `-N` for that
artifact whose commit is on `main` (usually `main` HEAD after that `-N`).

```bash
git tag <artifact>-<X.Y.Z> <artifact>-<highest-N>
git push origin <artifact>-<X.Y.Z>
```
