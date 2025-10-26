package com.fardeen.intervueai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fardeen.intevueai.model.ChatModel
import com.fardeen.intevueai.model.ChatsListingModel
import com.fardeen.intevueai.model.RequestState
import com.fardeen.intevueai.usecases.FetchChatListingDataUseCase
import com.fardeen.intevueai.usecases.FetchLocalDataUseCase
import com.fardeen.intevueai.usecases.SaveLocalChatUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class InterviewChatViewModel(
    private val useCase: FetchLocalDataUseCase,
    private val useCase1: SaveLocalChatUseCase,
    private val useCase2: FetchChatListingDataUseCase
) : ViewModel() {

    private val _chatData = MutableStateFlow<RequestState<List<ChatModel>>?>(null)
    val chatData = _chatData.asSharedFlow()


    private val _chatListData = MutableStateFlow<RequestState<List<ChatsListingModel>>?>(null)
    val chatListData = _chatListData.asStateFlow()

    private val _saveDataResult = MutableStateFlow<RequestState<String>?>(null)
    val saveDataResult = _saveDataResult.asStateFlow()


    val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()



    fun getLocalChatData() {
        viewModelScope.launch {
            useCase2()
                .onStart {
                    _chatListData.value = RequestState.Loading
                }
                .catch { e ->

                    _uiEvent.emit(UiEvent.ShowToast("Failed to receive chat"))
                    _chatListData.value = RequestState.Error(e.message ?: "Unknown error") }
                .collect { data ->

                    _chatListData.value = RequestState.Success(data)
                    _uiEvent.emit(UiEvent.ShowToast("Chat received successfully"))
                }

        }
    }


    fun getChatListingData() {

        viewModelScope.launch {
            useCase2()
                .onStart {
                    _chatListData.value = RequestState.Loading
                }
                .catch { e ->
                    _chatListData.value = RequestState.Error(e.message ?: "Unknown error")
                    }
                .collect { data ->
                    _chatListData.value = RequestState.Success(data)
                }

        }
    }

    fun saveLocalChat(chatModel: ChatModel) {
        viewModelScope.launch {
            useCase1(chatModel)
                .onStart { _saveDataResult.value = RequestState.Loading }
                .catch { e ->
                    _saveDataResult.value = RequestState.Error(e.message ?: "Unknown error")
                    _uiEvent.emit(UiEvent.ShowToast("Failed to save chat"))
                }
                .collect { data ->
                    _saveDataResult.value = RequestState.Success(data)
                    _uiEvent.emit(UiEvent.ShowToast("Chat saved successfully"))
                }
        }
    }


}