package com.barreto.android.domain.util

interface IClientPropertiesProvider {

//    val clientId: String

    val userAgent: String

    val accept: String get() = "application/vnd.barreto.api.v1+json"

    val device: Map<String, String>
}
