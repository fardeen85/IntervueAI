package com.fardeen.intevueai.usecases

import com.fardeen.intevueai.gateway.LocalRepository
import com.fardeen.intevueai.model.ChatModel
import kotlinx.coroutines.flow.Flow

class SaveLocalChatUseCase(val repository: LocalRepository){

    suspend operator fun invoke(chatModel: ChatModel): Flow<String>{
       return repository.saveToChat(chatModel)
    }

}