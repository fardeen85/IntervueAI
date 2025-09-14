package com.fardeen.intevueai.gateway

import com.fardeen.intevueai.model.ChatModel
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun getAllChatData() : Flow<List<ChatModel>>
    suspend fun  saveToChat(ChatEntity: List<ChatModel>)
}