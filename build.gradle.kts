plugins {
    kotlin("jvm") version "1.6.10"
    java
    id ("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "me.chaoticwagon"
version = "1.0"

repositories {
    mavenCentral()
    maven(url = "https://repo.spongepowered.org/maven")
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("com.github.Minestom:Minestom:-SNAPSHOT")
}

tasks {
    shadowJar{
        destinationDirectory.set(file("C:\\Users\\natha\\Documents\\MINESTOM\\EcoMC"))
        manifest {
            attributes (
                "Main-Class" to "me.chaoticwagon.ecomc.EcoMC",
                "Multi-Release" to true
            )
        }

    }
    test {
        useJUnitPlatform()
    }
}