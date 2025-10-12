package com.fardeen.intevueai.usecases

import com.fardeen.intevueai.gateway.LocalRepository
import com.fardeen.intevueai.model.ChatsListingModel
import kotlinx.coroutines.flow.Flow

class FetchChatListingDataUseCase(val repository: LocalRepository){

    suspend operator fun invoke(): Flow<List<ChatsListingModel>> {
        return repository.getAllChatListingData()
    }


}