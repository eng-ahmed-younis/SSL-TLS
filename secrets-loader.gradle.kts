import java.util.Properties

// Create a Properties object and load secrets.properties if it exists
val secretsFile = rootProject.file("secrets.properties")
val secretsProps = Properties()


if (secretsFile.exists()) {
    secretsFile.inputStream().use { secretsProps.load(it) }
    println("✅ Loaded secrets.properties with ${secretsProps.size} keys")
} else {
    println("⚠️ No secrets.properties found — using empty values")
}

// Expose to other build scripts via the Gradle extra properties map
extra["secrets"] = secretsProps
