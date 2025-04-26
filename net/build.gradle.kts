plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality"
version = "0.1.0"

dependencies {
    api(projects.buffer)
    api(projects.crypto)

    api(libs.netty.transport)
    api(libs.netty.handler)

    api(libs.netty.transport.native.epoll)
    api(libs.netty.transport.native.kqueue)
    api(libs.netty.incubator.transport.native.iouring)

    implementation(libs.slf4j.api)
    implementation(libs.fastutil)
    implementation(libs.google.guava)
}
