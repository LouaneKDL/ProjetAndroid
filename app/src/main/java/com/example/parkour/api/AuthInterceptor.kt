package com.example.parkour.api

import okhttp3.Interceptor
import okhttp3.Response

/**
 * This class automatically adds authorization and content type headers to each HTTP request.
 *
 * @property token: to authenticate HTTP requests
 *
 */

class AuthInterceptor(private val token:String) : Interceptor {

    /**
     *  modifies each HTTP request to include an authorization header and a JSON content acceptance header before transmitting it.
     *  @return the HTTP response obtained
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}