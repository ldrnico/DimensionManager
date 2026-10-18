# Paper Kotlin Plugin Template

A pre-configured Gradle template for Minecraft plugin development using **Kotlin**, **ShadowJar**, and the **`run-paper`** test server plugin.

## Configuration Guide (`build.gradle.kts`)

### 1. Paper API & Minecraft Server Version

Update the version numbers to match your targeted Minecraft version:

```kotlin
dependencies {
    compileOnly("io.papermc.paper:paper-api:YOUR_VERSION") // e.g., 1.20.4-R0.1-SNAPSHOT
}

tasks {
    runServer {
        minecraftVersion("YOUR_VERSION") // e.g., 1.20.4
    }
}
```

### 2. Java / JVM Toolchain

Set the Java version to match your installed JDK or server environment:

```kotlin
kotlin {
    jvmToolchain(21) // e.g., 17 or 21
}
```

### 3. Test Server RAM (Optional)

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
3. The Paper test server boots up with the updated plugin.
