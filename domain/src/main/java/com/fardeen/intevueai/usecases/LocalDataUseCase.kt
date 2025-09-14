package com.fardeen.intevueai.usecases

import com.fardeen.intevueai.gateway.LocalRepository
import com.fardeen.intevueai.model.ChatModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchLocalDataUseCase(val repository: LocalRepository) {

    suspend operator fun invoke(): Flow<List<ChatModel>>{
         return  repository.getAllChatData()

    }
}