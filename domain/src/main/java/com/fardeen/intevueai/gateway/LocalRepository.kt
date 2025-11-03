package com.fardeen.intevueai.gateway

import com.fardeen.intevueai.model.ChatModel
import com.fardeen.intevueai.model.ChatsListingModel
import com.fardeen.intevueai.model.Message
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun getAllChatData(id: Int) : Flow<List<Message>>
    suspend fun  saveToChat(ChatEntity:ChatModel):Flow<String>

    suspend fun getAllChatListingData() : Flow<List<ChatsListingModel>>

    suspend fun saveToChatListing(ChatEntity:ChatsListingModel):Flow<String>

    suspend fun deleteChatListing(id: Int):Flow<Int>

    suspend fun deleteChat(id: Int):Flow<Int>

}