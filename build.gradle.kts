import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
  //kotlin("jvm") version "1.9.0" // add this line to specify the kotlin version
  //kotlin("kapt") version "1.9.0" // now kapt should be recognised
}

group = "com.uniksoft"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.5.9"
val junitJupiterVersion = "5.9.1"

val mainVerticleName = "com.uniksoft.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  implementation("org.slf4j:slf4j-api:1.8.0")
  implementation("org.apache.logging.log4j:log4j-api:2.17.1")
  implementation("org.apache.logging.log4j:log4j-core:2.17.1")
  implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.1")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
  implementation("org.flywaydb:flyway-core:8.5.4")
  implementation("org.postgresql:postgresql:42.3.1")
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-pg-client:4.2.3")
  "compileOnly"("org.projectlombok:lombok:1.18.22")
  "annotationProcessor"("org.projectlombok:lombok:1.18.22")
  "testCompileOnly"("org.projectlombok:lombok:1.18.22")
  "testAnnotationProcessor"("org.projectlombok:lombok:1.18.22")
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
