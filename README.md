# Paper Kotlin Template (Minecraft 26.2)

Minimal Template for Paper Plugin Development using Kotlin, ShadowJar, and `run-paper`.

## Setup

Adjust the following for your project:

- **`gradle.properties`**: Update `group` (e.g., `com.example.plugin`).
- **`src/main/resources/plugin.yml`**: Update `name`, `main`, and other metadata.

## Run Server

Execute `./gradlew runServer` or run the `runServer` Gradle task. The build, ShadowJar packaging, and plugin deployment to `run/plugins` are handled automatically.
