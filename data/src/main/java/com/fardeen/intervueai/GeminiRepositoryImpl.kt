package com.fardeen.intervueai

import com.fardeen.intevueai.GeminiRepository
import com.fardeen.intevueai.model.GeminiRequest
import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestContent
import com.fardeen.intevueai.model.RequestPart
import com.fardeen.intevueai.model.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.internal.decodeStringToJsonTree


class GeminiRepositoryImpl : GeminiRepository {


    val ktorClient = HttpClient {

        install(ContentNegotiation) {

            json((Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }))
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }

//        install(DefaultRequest) {
//            headers.append("apiKey", API_KEY)
//
//        }
    }

    override suspend fun callGemini(prompt: String): RequestState<GeminiResponseModel> {

        return try {

            val response =
                ktorClient.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent") {
                    url {
                        parameters.append(
                            "key",
                            "AIzaSyAUInd2Jxb__3HJFV6SnsldbiW7XLXnto8"
                        )
                    }

                    setBody({

                        GeminiRequest(
                            contents = listOf(
                                RequestContent(
                                    parts = listOf(
                                        RequestPart(
                                            text = prompt
                                        )
                                    )
                                )
                            )
                        )
                    })
                }
            if (response.status.value == 200) {

                val apiResponse = Json.decodeFromString<GeminiResponseModel>(response.body())
                RequestState.Success(apiResponse)
            } else {
                RequestState.Error(response.status.description)
            }

        } catch (e: Exception) {

            RequestState.Error(e.message ?: "Unknown Error")
        }


    }
}