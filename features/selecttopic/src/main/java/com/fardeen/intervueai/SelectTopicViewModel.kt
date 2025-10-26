package com.fardeen.intervueai

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fardeen.intevueai.gateway.GeminiRepository
import com.fardeen.intevueai.model.ChatsListingModel
import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestState
import com.fardeen.intevueai.usecases.CallGeminiUseCase
import com.fardeen.intevueai.usecases.FetchChatListingDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout


class SelectTopicViewModel (
    private val callGeminiUseCase: CallGeminiUseCase,
    private val fetchChatListingDataUseCase: FetchChatListingDataUseCase,
    val useCase3: FetchChatListingDataUseCase
) : ViewModel() {

    private val _verifyTopic = MutableStateFlow<RequestState<GeminiResponseModel>>(RequestState.Idl)
    val verifyTopic: StateFlow<RequestState<GeminiResponseModel>> = _verifyTopic


    private val _chatLisingData = MutableStateFlow<RequestState<List<ChatsListingModel>?>?>(null)
    val chatLisingData = _chatLisingData.asStateFlow()



    private val _chatListingData = MutableStateFlow<RequestState<List<ChatsListingModel>>>(RequestState.Idl)
    val chatListingData: StateFlow<RequestState<List<ChatsListingModel>>> = _chatListingData

    /** Combines both states so UI can observe a single stream if needed */
    val combinedState: StateFlow<CombinedState> = combine(
        verifyTopic,
        chatListingData
    ) { gemini, chatList ->
        CombinedState(geminiResult = gemini, chatListResult = chatList)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CombinedState(RequestState.Idl, RequestState.Idl)
    )

    /**
     * Call Gemini API after ensuring chat listing data is loaded.
     */
    fun verifyTopicByGemini(prompt: String) {
        viewModelScope.launch {
            // Ensure chat listing data exists before verification
            checkIfListingDataExists()

            callGeminiUseCase("is this a real topic to start interview practice reply with only Y or N only  the topic is "+prompt)
                .onStart { _verifyTopic.value = RequestState.Loading }
                .catch { e ->
                    Log.e("SelectTopicViewModel", "verifyTopicByGemini failed", e)
                    _verifyTopic.value = RequestState.Error(e.message ?: "Unknown error")
                }
                .collect { data ->
                    _verifyTopic.value = RequestState.Success(data)
                }
        }
    }

    /**
     * Loads existing chat listings.
     */
    fun checkIfListingDataExists() {
        viewModelScope.launch {
            fetchChatListingDataUseCase()
                .onStart { _chatListingData.value = RequestState.Loading }
                .catch { e ->
                    Log.e("SelectTopicViewModel", "checkIfListingDataExists failed", e)
                    _chatListingData.value = RequestState.Error(e.message ?: "Unknown error")
                }
                .collect { data ->
                    _chatListingData.value = RequestState.Success(data)
                }
        }
    }

    fun resetCombinedState() {
        _verifyTopic.value = RequestState.Idl
        _chatListingData.value = RequestState.Idl
    }



    fun getChatListingData(){
        viewModelScope.launch {

            useCase3()
                .onStart {
                    _chatLisingData.value = RequestState.Loading
                }
                .catch { e->
                    _chatLisingData.value = RequestState.Error(e.message?:"")
                }
                .collect { data->
                    _chatLisingData.value = RequestState.Success(data)
                }
        }
    }




}


data class CombinedState(
    val geminiResult: RequestState<GeminiResponseModel?>?,
    val chatListResult: RequestState<List<ChatsListingModel>>?
)
