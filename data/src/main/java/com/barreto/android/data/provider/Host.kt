package com.barreto.android.data.provider

enum class Host(val baseUrl: String) {
    RELEASE("https://desafio.mobfiq.com.br/"),
    DEBUG("https://desafio.mobfiq.com.br/"),
    DEV("https://desafio.mobfiq.com.br/");

    companion object {
        fun fromBuildType(flavor: String, buildType: String): Host {
            return valueOf("${flavor}_$buildType".toUpperCase())
        }
        fun fromBuildType(buildType: String): Host {
            return valueOf(buildType.toUpperCase())
        }
    }
}
