package com.fardeen.intervueai

import com.fardeen.intevueai.model.Message
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fardeen.intevueai.model.ChatModel
import com.fardeen.intevueai.model.ChatsListingModel
import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestState
import com.fardeen.intevueai.usecases.CallGeminiUseCase
import com.fardeen.intevueai.usecases.DeleteChatListingUseCase
import com.fardeen.intevueai.usecases.DeleteChatUseCase
import com.fardeen.intevueai.usecases.FetchChatListingDataUseCase
import com.fardeen.intevueai.usecases.FetchLocalDataUseCase
import com.fardeen.intevueai.usecases.SaveLocalChatUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class InterviewChatViewModel(
    private val fetchChatMessages: FetchLocalDataUseCase,
    private val saveChatUseCase: SaveLocalChatUseCase,
    private val fetchChatList: FetchChatListingDataUseCase,
    private val callGemini: CallGeminiUseCase,
    private val deleteChatLising: DeleteChatListingUseCase,
    private val deleteChat: DeleteChatUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterviewChatUiState())
    val uiState: StateFlow<InterviewChatUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun loadChatList() {
        viewModelScope.launch {
            fetchChatList()
                .onStart {
                    _uiState.update { it.copy(chatList = RequestState.Loading, isLoading = true) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            chatList = RequestState.Error(e.message ?: "Error fetching chat list"),
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }
                    _uiEvent.send(UiEvent.Toast("Failed to load chat list"))
                }
                .collect { chats ->
                    _uiState.update {
                        it.copy(
                            chatList = RequestState.Success(chats),
                            isLoading = false
                        )
                    }
                    _uiEvent.send(UiEvent.Toast("Chats loaded"))
                }
        }
    }


    fun updateSelectedChatId(selectedChatId: Int?){

        _uiState.update {
           it.copy( selectedChatId = selectedChatId)
        }

    }


    fun updateChatName(chatName: String) {
        _uiState.update { it.copy(chatName = chatName) }
    }


    fun loadMessages() {
        viewModelScope.launch {
            Log.d("TAG","called with  ${uiState.value.selectedChatId?:1}")
            fetchChatMessages(uiState.value.selectedChatId?:1)
                .onStart { _uiState.update { it.copy(chats = RequestState.Loading, selectedChatId = uiState.value.selectedChatId?:1) } }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            chats = RequestState.Error(e.message ?: "Error loading messages"),
                            errorMessage = e.message
                        )
                    }
                }
                .collect { messages ->
                    Log.d("TAG","recevied ${messages}")
                    _uiState.update { it.copy(chats = RequestState.Success(messages)) }
                }
        }
    }


    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            // 1️⃣ Save user message locally first
            saveChat(ChatModel(message = prompt, messageOwner = "user", chatId = uiState.value.selectedChatId?:1))

            // 2️⃣ Call Gemini
            callGemini(prompt)
                .onStart {
                    _uiState.update { it.copy(geminiResponse = RequestState.Loading, isLoading = true) }
                    loadMessages()
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            geminiResponse = RequestState.Error(e.message ?: "Gemini failed"),
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }
                    loadMessages()
                }
                .collect { geminiResponse ->
                    val reply = geminiResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()

                    // Save AI response
                    saveChat(ChatModel(message = reply, messageOwner = "AI", chatId = uiState.value.selectedChatId?:1))

                    // Update UI state
                    _uiState.update {
                        it.copy(
                            geminiResponse = RequestState.Success(geminiResponse),
                            isLoading = false

                        )
                    }

                    loadMessages()
                }
        }
    }


    private suspend fun saveChat(chat: ChatModel) {
        saveChatUseCase(chat)
            .onStart { _uiState.update { it.copy(saveStatus = RequestState.Loading) } }
            .catch { e ->
                _uiState.update { it.copy(saveStatus = RequestState.Error(e.message ?: "Failed to save chat")) }
                _uiEvent.send(UiEvent.Toast("Failed to save chat"))
            }
            .collect { result ->
                _uiState.update { it.copy(saveStatus = RequestState.Success(result)) }
            }
    }


    fun sendShowDialogEvent(){

        viewModelScope.launch {
            _uiEvent.send(UiEvent.Dialog.ShowSuccess)
        }

    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }


    fun deleteChat(){

        viewModelScope.launch {

            uiState.value.selectedChatId?.let {

                deleteChat(it)
                    .onStart {}
                    .catch { e ->

                        _uiEvent.send(UiEvent.Toast("Failed to delete chat"))
                    }
                    .collect {

                        _uiEvent.send(UiEvent.Toast("Chat deleted"))

                    }

            }



        }
    }
    fun deleteChatListItem(){

        viewModelScope.launch {

            uiState.value.selectedChatId?.let {

                deleteChatLising(it)
                    .onStart {
                        _uiEvent.send(UiEvent.Dialog.ShowLoading)
                    }
                    .catch { e ->

                        _uiEvent.send(UiEvent.Toast("Failed to delete chat"))
                    }
                    .collect {

                        _uiEvent.send(UiEvent.Toast("Chat deleted"))
                        _uiEvent.send(UiEvent.Dialog.HideLoading)
                        deleteChat()
                        loadChatList()

                    }

            }



        }
    }
}

data class InterviewChatUiState(
    val chatName: String?="",
    val chats: RequestState<List<Message>> = RequestState.Idl,
    val chatList: RequestState<List<ChatsListingModel>> = RequestState.Idl,
    val geminiResponse: RequestState<GeminiResponseModel?> = RequestState.Idl,
    val saveStatus: RequestState<String?> = RequestState.Idl,
    val selectedChatId: Int? = 1,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
