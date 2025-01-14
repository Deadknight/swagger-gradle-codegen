

group = rootProject.group
version = rootProject.version

plugins {
    id("java")
    id("java-gradle-plugin")
    id("maven-publish")
    id("jacoco")
    kotlin("jvm") version "1.4.32"
    id("com.gradle.plugin-publish") version "0.15.0"
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    id("com.github.ben-manes.versions") version "0.39.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

jacoco {
    toolVersion = "0.8.5"
}

dependencies {
    implementation(gradleApi())

    implementation("commons-cli:commons-cli:1.4")
    implementation("com.google.guava:guava") {
        version {
            // We force the version otherwise we get a -android version that will break when used with AGP.
            strictly("28.2-jre")
        }
    }
    implementation("io.swagger:swagger-codegen:2.4.21")

    testImplementation("junit:junit:4.13.2")
}

tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allJava)
    classifier = "sources"
}
gradlePlugin {
    plugins {
        create("com.yelp.codegen.plugin") {
            id = "com.yelp.codegen.plugin"
            implementationClass = "com.yelp.codegen.plugin.CodegenPlugin"
        }
    }
}

// Configuration Block for the Plugin Marker artifact on Plugin Central
pluginBundle {
    website = "https://github.com/Yelp/swagger-gradle-codegen"
    vcsUrl = "https://github.com/Yelp/swagger-gradle-codegen"
    description = "A Gradle plugin to generate networking code from Swagger Specs"
    tags = listOf("swagger", "codegen", "retrofit", "android", "kotlin", "networking")

    plugins {
        getByName("com.yelp.codegen.plugin") {
            displayName = "Swagger Gradle Codegen"
        }
    }
}

configure<PublishingExtension> {
    // Add a local repository for tests.
    // The plugin tests will use this repository to retrieve the plugin artifact.
    // This allows to test the current code without deploying it to the gradle
    // portal or any other repo.
    repositories {
        maven {
            name = "pluginTest"
            url = uri("file://${rootProject.buildDir}/localMaven")
        }
    }
}

/*detekt {
    input = files("src/")
    config = rootProject.files("../config/detekt/detekt.yml")
}*/

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<Test> {
    dependsOn("publishPluginMavenPublicationToPluginTestRepository")
    inputs.dir("src/test/testProject")
}
