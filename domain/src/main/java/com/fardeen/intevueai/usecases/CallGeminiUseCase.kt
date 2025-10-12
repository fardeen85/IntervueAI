package com.fardeen.intevueai.usecases

import com.fardeen.intevueai.gateway.GeminiRepository
import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestState
import kotlinx.coroutines.flow.Flow

class CallGeminiUseCase(val repository: GeminiRepository) {

     operator fun invoke(prompt: String): Flow<GeminiResponseModel>{

       return repository.callGemini(prompt)
    }




}