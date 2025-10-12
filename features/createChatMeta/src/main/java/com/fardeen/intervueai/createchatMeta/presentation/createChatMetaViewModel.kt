package com.fardeen.intervueai.createchatMeta.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fardeen.intevueai.model.ChatsListingModel
import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestState
import com.fardeen.intevueai.usecases.FetchLocalDataUseCase
import com.fardeen.intevueai.usecases.SaveChatListingDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class createChatMetaViewModel(val useCase1: SaveChatListingDataUseCase, val useCase2: FetchLocalDataUseCase) : ViewModel(){


    private val _chatLisingData = MutableStateFlow<RequestState<ChatsListingModel?>?>(null)
    val chatLisingData = _chatLisingData.asStateFlow()


    private val _addChatListingData = MutableStateFlow< RequestState<String?>?>(null)
    private val addChatListingData = _addChatListingData.asSharedFlow()


    fun saveChatListingData(chatListingData: ChatsListingModel) {


        viewModelScope.launch {

            useCase1(chatListingData)
                .onStart { _addChatListingData.value = RequestState.Loading }
                .catch { e -> _addChatListingData.value = RequestState.Error(e.message ?: "Unknown error") }
                .collect { data ->
                    _addChatListingData.value = RequestState.Success(data)
                }
        }

    }

}