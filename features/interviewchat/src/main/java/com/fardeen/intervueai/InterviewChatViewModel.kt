package com.fardeen.intervueai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fardeen.intevueai.model.ChatModel
import com.fardeen.intevueai.model.RequestState
import com.fardeen.intevueai.usecases.FetchLocalDataUseCase
import com.fardeen.intevueai.usecases.SaveLocalChatUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class InterviewChatViewModel(
    private val useCase: FetchLocalDataUseCase,
    private val useCase1: SaveLocalChatUseCase
) : ViewModel() {

    private val _chatData = MutableStateFlow<RequestState<List<ChatModel>>?>(null)
    val chatData = _chatData.asStateFlow()

    private val _saveDataResult = MutableStateFlow<RequestState<String>?>(null)
    val saveDataResult = _saveDataResult.asStateFlow()


    fun getLocalChatData() {
        viewModelScope.launch {
            useCase()
                .onStart { _chatData.value = RequestState.Loading }
                .catch { e -> _chatData.value = RequestState.Error(e.message ?: "Unknown error") }
                .collect { data ->
                    _chatData.value = RequestState.Success(data)
                }

        }
    }

    fun saveLocalChat(chatModel: ChatModel) {

        viewModelScope.launch {

            useCase1(chatModel)
                .onStart { _saveDataResult.value = RequestState.Loading }
                .catch { e ->
                    _saveDataResult.value = RequestState.Error(e.message ?: "Unknown error")
                }
                .collect { data ->
                    _saveDataResult.value = RequestState.Success(data)
                }
        }
    }


}