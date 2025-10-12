package com.fardeen.intervueai

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fardeen.intevueai.gateway.GeminiRepository
import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestState
import com.fardeen.intevueai.usecases.CallGeminiUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class SelectTopicViewModel(private val useCase: CallGeminiUseCase) : ViewModel() {


    private val _verifyTopic = MutableStateFlow<RequestState<GeminiResponseModel?>?>(null)
    val verifyTopic = _verifyTopic.asStateFlow()

    fun verifyTopicByGemini(prompt: String) {


        viewModelScope.launch{
            useCase(prompt)
                .onStart {
                    Log.d("TAG","collect start")
                    _verifyTopic.value = RequestState.Loading }
                .catch { e ->
                    Log.d("TAG","failure flow ${e.message}")
                    _verifyTopic.value = RequestState.Error(e.message ?: "Unknown error") }
                .collect { data ->
                    _verifyTopic.value = RequestState.Success(data)
                }
        }

    }




}