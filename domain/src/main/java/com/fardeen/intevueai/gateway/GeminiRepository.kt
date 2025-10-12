package com.fardeen.intevueai.gateway

import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestState
import kotlinx.coroutines.flow.Flow

interface GeminiRepository {

     fun callGemini(prompt:String) : Flow<GeminiResponseModel>



}