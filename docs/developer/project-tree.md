# Project Structure

`openvino-notes` is an Android multi-module project. The product direction is an AI-assisted notes app powered by OpenVINO, but the current repository is still in an early implementation stage.


## Modules

| Module | Responsibility | Current state |
| --- | --- | --- |
| `:app` | Android app module, Compose UI, app wiring | Basic shell, starter UI, debug and androidTest APKs are produced here |
| `:domain` | Models, repository contracts, use cases | Mostly placeholder contracts and use cases |
| `:data` | Repository implementations, storage, mapping | Structure exists, implementation is still minimal |
| `:ai` | OpenVINO-facing inference and result processing | Integration points exist, production behavior is not implemented yet |

## Build and Quality

Root build logic lives in [build.gradle.kts](/Users/anesterov/repos/openvino-notes/build.gradle.kts).

Shared tooling:

- `ktlint`
- `detekt`
- Android Lint
- `kover`
- dependency locking

Key versions:

- JDK 17
- compileSdk 36
- targetSdk 36
- minSdk 33
- Kotlin 2.0.21
- Android Gradle Plugin 8.13.2

## CI Layout

The CI entry point is `.github/workflows/ci.yml`.

Main reusable workflows:

| Workflow | Purpose |
| --- | --- |
| `preflight.yml` | Decides whether expensive jobs should run |
| `quality.yml` | Formatting, linting, debug build, unit tests, coverage |
| `security.yml` | Gitleaks |
| `release.yml` | Release assemble and release lint |
| `android-tests.yml` | APK validation and emulator instrumentation |
| `codeql.yml` | CodeQL-oriented build and analysis |
| `supply-chain.yml` | Dependency review on pull requests |

Most command logic is intentionally kept in `.github/scripts/` rather than embedded directly in workflow YAML.

## Contributor Notes

- If you change build logic, inspect `.github/scripts/` before editing workflow YAML.
- If you change module boundaries, update `settings.gradle.kts`, relevant module build files, and docs together.
- Many current tests are scaffolding tests, so build or packaging regressions are often the first thing to check when a CI job fails.
