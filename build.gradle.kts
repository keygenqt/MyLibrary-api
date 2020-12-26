buildscript {
    dependencies {
        classpath("com.h2database:h2:1.4.197")
    }
}

plugins {
    id("org.springframework.boot") version "2.3.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    war
    java
    id("org.flywaydb.flyway") version "7.0.0"
}

group = "com.keygenqt"
version = "1.0.0"
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

// commons-io
dependencies {
    implementation("org.apache.commons:commons-io:1.3.2")
}

// rest
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
}

// mysql
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java:8.0.18")
}

// security
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
}

// validate
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("commons-validator:commons-validator:1.7")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}
