import org.gradle.api.tasks.PathSensitivity

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:12.6.1")
    }
}

plugins {
    id("java")
    id("com.diffplug.spotless") version "8.1.0"
    id("checkstyle")
    id("pmd")
    id("org.flywaydb.flyway") version "12.6.1"
    id("org.jooq.jooq-codegen-gradle") version "3.21.5"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val jooqGeneratedDir = layout.buildDirectory.dir("generated-sources/jooq")

configurations {
    create("flywayMigration")
}

dependencies {
    implementation("io.javalin:javalin:7.2.2")
    implementation("io.javalin:javalin-bundle:7.2.2")

    implementation("org.postgresql:postgresql:42.7.11")
    implementation("org.jooq:jooq:3.21.5")

    implementation("org.flywaydb:flyway-core:12.6.1")
    implementation("org.flywaydb:flyway-database-postgresql:12.6.1")

    add("flywayMigration", "org.postgresql:postgresql:42.7.11")
    add("flywayMigration", "org.flywaydb:flyway-database-postgresql:12.6.1")

    jooqCodegen("org.postgresql:postgresql:42.7.11")

    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.3")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation(platform("org.junit:junit-bom:5.11.0"))

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.3")
}

sourceSets {
    main {
        java {
            srcDir(jooqGeneratedDir)
        }
    }
}

flyway {
    configurations = arrayOf("flywayMigration")
    url = "jdbc:postgresql://localhost:5432/javalin"
    user = "postgres"
    password = "postgres"
    locations = arrayOf("filesystem:src/main/resources/db/migration")
}

jooq {
    configuration {
        jdbc {
            driver = "org.postgresql.Driver"
            url = flyway.url
            user = flyway.user
            password = flyway.password
        }
        generator {
            name = "org.jooq.codegen.DefaultGenerator"
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                inputSchema = "public"
                includes = ".*"
                excludes = "flyway_schema_history"
            }
            target {
                packageName = "org.example.jooq.generated"
                directory = jooqGeneratedDir.get().asFile.absolutePath
            }
        }
    }
}

tasks.named("jooqCodegen") {
    dependsOn("flywayMigrate")
    inputs.files(fileTree("src/main/resources/db/migration"))
        .withPropertyName("migrations")
        .withPathSensitivity(PathSensitivity.RELATIVE)
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn("jooqCodegen")
}

tasks.test {
    useJUnitPlatform()
    environment("JDBC_URL", "jdbc:postgresql://localhost:5432/javalin_test")
}

spotless { // pre-commit
    java {
        target("src/**/*.java")
        targetExclude("**/build/**")
        googleJavaFormat("1.33.0")
        trimTrailingWhitespace()
        endWithNewline()
        removeUnusedImports()
    }
}

checkstyle { // pre-commit
    toolVersion = "12.1.2"
    configFile = file("config/checkstyle/checkstyle.xml")
    configProperties = mapOf(
        "org.checkstyle.google.suppressionfilter.config" to
                file("config/checkstyle/checkstyle-suppressions.xml").absolutePath
    )
}

tasks.withType<Checkstyle>().configureEach {
    exclude("**/org/example/jooq/generated/**")
}

pmd { // pre-commit
    toolVersion = "6.55.0"
}
