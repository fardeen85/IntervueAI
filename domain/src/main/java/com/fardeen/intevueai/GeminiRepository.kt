package com.fardeen.intevueai

import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestState

interface GeminiRepository {

    suspend fun callGemini(prompt:String) : RequestState<GeminiResponseModel>

}