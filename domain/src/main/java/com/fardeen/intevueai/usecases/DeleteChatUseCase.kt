package com.fardeen.intevueai.usecases

import com.fardeen.intevueai.gateway.LocalRepository
import kotlinx.coroutines.flow.Flow

class DeleteChatUseCase(val repository: LocalRepository){


    suspend operator fun invoke(id: Int): Flow<Int>{

        return  repository.deleteChat(id)

    }
}