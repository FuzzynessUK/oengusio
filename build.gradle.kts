plugins {
    java
    application

    id("org.springframework.boot") version "2.1.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.RELEASE")
    }
}

project.group = "app.oengus"
project.version = "2020.48.0"

java {
    sourceCompatibility = JavaVersion.VERSION_12
    targetCompatibility = JavaVersion.VERSION_12
}

application {
    mainClass.set("${project.group}.OengusApplication")
}

repositories {
    jcenter()
}

dependencies {
    // SPRING
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-validation")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-actuator")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-security")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-web")
    implementation(group = "org.liquibase", name = "liquibase-core")
    implementation(group = "io.micrometer", name = "micrometer-registry-prometheus")
    
    // POSTGRESQL
    runtimeOnly(group = "org.postgresql", name = "postgresql")
    
    // JWT
    implementation(group = "io.jsonwebtoken", name = "jjwt", version = "0.9.1")
    implementation(group = "javax.xml.bind", name = "jaxb-api", version = "2.3.1")
    
    // APACHE
    implementation(group = "org.apache.commons", name = "commons-lang3", version = "3.9")
    implementation(group = "org.apache.commons", name = "commons-csv", version = "1.7")
    
    // FEIGN
    implementation(group = "org.springframework.cloud", name = "spring-cloud-starter-openfeign", version = "2.1.1.RELEASE")
    
    // JACKSON
    implementation(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310", version = "2.9.8")

    // HIBERNATE
    implementation(group = "com.vladmihalcea", name = "hibernate-types-52", version = "2.5.0")
    implementation(group = "org.hibernate", name = "hibernate-ehcache", version = "5.3.9.Final")

    // JAVERS
    implementation(group = "org.javers", name = "javers-core", version = "5.6.2")
    implementation(group = "org.javers", name = "javers-persistence-sql", version = "5.6.2")
    implementation(group = "org.javers", name = "javers-spring-boot-starter-sql", version = "5.6.2")
    implementation(group = "com.google.guava", name = "guava", version = "27.0.1-jre")

    // SWAGGER
    implementation(group = "io.springfox", name = "springfox-swagger2", version = "2.9.2")
    implementation(group = "io.springfox", name = "springfox-swagger-ui", version = "2.9.2")

    // iCal4J
    implementation(group = "org.mnode.ical4j", name = "ical4j", version = "3.0.11")

    // Paypal
    implementation(group = "com.paypal.sdk", name = "checkout-sdk", version = "1.0.2")

    // Twitter
    implementation(group = "org.twitter4j", name = "twitter4j-core", version = "4.0.7")
}

val wrapper: Wrapper by tasks

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true
    options.compilerArgs = listOf("-Xlint:deprecation", "-Xlint:unchecked")
}

wrapper.apply {
    gradleVersion = "6.7.1"
    distributionType = Wrapper.DistributionType.ALL
}