package com.fardeen.intevueai.usecases

import com.fardeen.intevueai.gateway.LocalRepository
import com.fardeen.intevueai.model.ChatModel
import com.fardeen.intevueai.model.Message
import kotlinx.coroutines.flow.Flow

class FetchLocalDataUseCase(val repository: LocalRepository) {

    suspend operator fun invoke(id: Int): Flow<List<Message>>{
         return  repository.getAllChatData(id)

    }
}