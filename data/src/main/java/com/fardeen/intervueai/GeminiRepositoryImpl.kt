package com.fardeen.intervueai

import android.util.Log
import com.fardeen.intervueai.remote.KtorClient
import com.fardeen.intervueai.remote.RemoteDataSource
import com.fardeen.intevueai.gateway.GeminiRepository
import com.fardeen.intevueai.model.GeminiRequest
import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestContent
import com.fardeen.intevueai.model.RequestPart
import com.fardeen.intevueai.model.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class GeminiRepositoryImpl(val remoteDataSource: RemoteDataSource) : GeminiRepository {


    override  fun callGemini(prompt: String): Flow<GeminiResponseModel>{

        return flow { emit(remoteDataSource.callGeminiAPI(prompt)) }.flowOn(Dispatchers.IO)
    }







}
