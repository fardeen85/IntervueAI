package com.fardeen.intervueai.remote

import android.util.Log
import com.fardeen.intevueai.model.GeminiRequest
import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestContent
import com.fardeen.intevueai.model.RequestPart
import com.fardeen.intevueai.model.RequestState
import com.fardeen.intevueai.model.UsageMetadata
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlinx.serialization.json.Json

class RemoteDataSource {





    suspend fun callGeminiAPI(prompt: String): GeminiResponseModel {

        return try {

            KtorClient.client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent") {
                url {
                    parameters.append("key", "AIzaSyAUInd2Jxb__3HJFV6SnsldbiW7XLXnto8")
                }

                contentType(ContentType.Application.Json)
                setBody(
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
                )
            }.body()


            /*  if (response.status == HttpStatusCode.OK) {
                  val responseBody = response.bodyAsText()
                  val apiResponse = Json.decodeFromString<GeminiResponseModel>(responseBody)
                  emit(RequestState.Success(apiResponse))
              } else {
                  emit(RequestState.Error(response.status.description))
              }

          } catch (e: Exception) {
              emit(RequestState.Error(e.message ?: "Unknown Error"))
          }*/
        } catch (e: Exception) {

            Log.d("TAG","exception in parsing model ${e.message}")
            e.printStackTrace()
            GeminiResponseModel(
                candidates = emptyList(),
                usageMetadata = UsageMetadata(
                    promptTokenCount = 0,
                    candidatesTokenCount = 0,
                    totalTokenCount = 0,
                    promptTokensDetails = emptyList(),
                    candidatesTokensDetails = emptyList()
                ),
                modelVersion = "0",
                responseId = "0"
            )

        }
    }
}