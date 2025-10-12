package com.fardeen.intevueai.usecases

import com.fardeen.intevueai.gateway.LocalRepository
import com.fardeen.intevueai.model.ChatsListingModel
import kotlinx.coroutines.flow.Flow

class SaveChatListingDataUseCase(val repository: LocalRepository){

    suspend operator fun invoke(chatListingModel: ChatsListingModel): Flow<String> {
        return repository.saveToChatListing(chatListingModel)
    }


}