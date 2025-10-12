package com.fardeen.intervueai.remote

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.logging.Logger
import kotlinx.serialization.json.Json
import okhttp3.internal.http2.Http2Reader.Companion.logger

object KtorClient {


    val client = HttpClient(OkHttp){


        install(ContentNegotiation) {

            json((Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }))
        }

      /*  install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }
*/

        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            socketTimeoutMillis = 10_000
            connectTimeoutMillis = 10_000
        }


        install(Logging) {
            logger =  object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    Log.d("KtorLogger", message)  // <-- goes to Logcat
                }
            }
            level = LogLevel.ALL
           /* filter { request ->
                request.url.host.contains("ktor.io")
            }*/
            sanitizeHeader { header -> header == HttpHeaders.Authorization }

        }
//        install(DefaultRequest) {
//            headers.append("apiKey", API_KEY)
//
//        }
    }
}