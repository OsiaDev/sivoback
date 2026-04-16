plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.coljuegos"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts/")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.oracle.database.jdbc:ojdbc11")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-mail")

	// JasperReports 5.2.0
	implementation("net.sf.jasperreports:jasperreports:5.2.0")
	// Groovy 3.0.19 para compatibilidad con Java 17
	implementation("org.codehaus.groovy:groovy:3.0.19")
	implementation("org.codehaus.groovy:groovy-jsr223:3.0.19")
	implementation("org.codehaus.groovy:groovy-json:3.0.19")
	implementation("org.codehaus.groovy:groovy-xml:3.0.19")

	implementation("com.sun.jndi:ldap")

    //runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}

tasks.withType<JavaExec> {
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}
