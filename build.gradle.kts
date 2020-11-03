import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("com.h2database:h2:1.4.197")
    }
}

plugins {
    id("org.springframework.boot") version "2.3.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    war
    id("org.flywaydb.flyway") version "7.0.0"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
}

group = "com.keygenqt"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

// flyway conf
flyway {
    url = "jdbc:mysql://localhost:3306/my_library?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
    user = "root"
    schemas = arrayOf("my_library")
    password = findProperty("password") as String
}

// mysql
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
}

// mysql
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    providedRuntime("mysql:mysql-connector-java")
}

// security
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}