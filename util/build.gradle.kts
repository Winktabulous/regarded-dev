plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality"
version = "0.1.0"

dependencies {
    api(libs.slf4j.api)
    api(libs.fastutil)
    implementation(libs.apache.commons.text)
    implementation(libs.kotlinx.datetime)
    api(libs.hikari.cp)
    api(libs.mysql.connector.j)
    api(libs.google.gson)
}
