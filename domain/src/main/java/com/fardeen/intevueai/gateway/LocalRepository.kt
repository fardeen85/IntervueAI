package com.fardeen.intevueai.gateway

import com.fardeen.intevueai.model.ChatModel
import com.fardeen.intevueai.model.ChatsListingModel
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun getAllChatData() : Flow<List<ChatModel>>
    suspend fun  saveToChat(ChatEntity:ChatModel):Flow<String>

    suspend fun getAllChatListingData() : Flow<List<ChatsListingModel>>

    suspend fun saveToChatListing(ChatEntity:ChatsListingModel):Flow<String>
}