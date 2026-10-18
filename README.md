# Paper Kotlin Plugin Template

A pre-configured Gradle template for Minecraft 26.2 plugin development using **Kotlin**, **ShadowJar**, and the **`run-paper`** test server plugin.

## Configuration Guide (`build.gradle.kts`)

### Test Server RAM (Optional)

Adjust the allocated memory for the test server as needed:

```kotlin
tasks {
    runServer {
        jvmArgs("-Xms2G", "-Xmx2G")
    }
}
```

## Running the Server & Development

Manual copying of the plugin file is not required.

Run the following command in your terminal, or select `Tasks -> run -> runServer` in the IntelliJ Gradle tab:

```bash
./gradlew runServer
```

**Automated Build Process:**

1. The plugin is built using `shadowJar`.
2. The output JAR is automatically copied to the internal test server directory (`run/plugins`).
3. The Paper test server boots up with the updated plugin on version 26.2.
