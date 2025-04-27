import org.gradle.kotlin.dsl.named
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.polarbookshop"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2024.0.1"

dependencies {
	implementation("org.springframework.cloud:spring-cloud-config-server")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<BootBuildImage>("bootBuildImage") {
	imageName.set("${project.name}")
	environment.set(
		mapOf(
			"BP_JVM_VERSION" to "21.*"
		)
	)
	publish.set(true)
	docker {
		publishRegistry {
			var rName = project.findProperty("registryUsername") as String?
			var rToken = project.findProperty("registryToken") as String?
			var rUrl = project.findProperty("registryUrl") as String?
			username.set(rName)
			password.set(rToken)
			url.set(rUrl)
		}
	}
}
