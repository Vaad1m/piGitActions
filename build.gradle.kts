plugins {
    id("java")
    id("pmd")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.3")
}

tasks.test {
    useJUnitPlatform()
}

pmd {
    toolVersion = "7.23.0"
    ruleSetFiles = files("pmd.xml")
    ruleSets = listOf()
}
