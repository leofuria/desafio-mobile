package com.barreto.android.data.provider

import com.barreto.android.domain.util.IClientPropertiesProvider
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeadersInterceptor(
        private val headersProvider: IClientPropertiesProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response? {
        val originalRequest = chain.request()

        val headersBuilder = originalRequest.headers().newBuilder()

        headersBuilder.addIfNotPresent("Accept", headersProvider.accept)
        headersBuilder.addIfNotPresent("User-Agent", headersProvider.userAgent)

//        val clientId = headersProvider.clientId
//        if (clientId.isNotEmpty()) {
//            headersBuilder.set("Client", clientId)
//        }

        val requestBuilder = originalRequest.newBuilder()
        requestBuilder.headers(headersBuilder.build())

        var response: Response? = null
        var exception: IOException? = null
        var tryCount = 0
        val maxTryCount = 3

        do {
            try {
                response = chain.proceed(requestBuilder.build())
            } catch (error: IOException) {
                exception = error
            } finally {
                tryCount++
            }
        } while (tryCount <= maxTryCount && (response == null || !response.isSuccessful))

        if (response == null && exception != null) throw exception

        return response

        /*var response = chain.proceed(requestBuilder.build())

        if (token != null && token != "" && (response.code() == 401 || response.code() == 403)) {
            // If there's a token and response code returns 401 or 403, include Bearer key in token
            // to try to solve conflicts on clientID header

            response = chain.proceed(
                    response.request()
                            .newBuilder()
                            .header("Authorization", "Bearer $token")
                            .build())

            // TODO Check if response code returns 401 or 403 again, if yes request user login
            //if(response.code() == 401 || response.code() == 403) {}
        }

        return response*/
    }
}
