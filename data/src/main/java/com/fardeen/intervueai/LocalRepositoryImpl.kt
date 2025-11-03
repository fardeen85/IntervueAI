package com.fardeen.intervueai

import com.fardeen.intervueai.local.ChatEntity
import com.fardeen.intervueai.local.LocalDataSource
import com.fardeen.intervueai.mapper.toDomain
import com.fardeen.intervueai.mapper.toEntity
import com.fardeen.intervueai.mapper.toMessage
import com.fardeen.intevueai.gateway.LocalRepository
import com.fardeen.intevueai.model.ChatModel
import com.fardeen.intevueai.model.ChatsListingModel
import com.fardeen.intevueai.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocalRepositoryImpl(private val localDataSource: LocalDataSource) : LocalRepository {
    override suspend fun getAllChatData(id: Int): Flow<List<Message>> {
        return flow { emit(localDataSource.getAllChatData(id).map { it.toMessage() })}.flowOn(   Dispatchers.IO)

    }

    override suspend fun saveToChat(ChatEntity:ChatModel): Flow<String>{

        return flow { emit(localDataSource.saveChatData(ChatEntity.toEntity())) }.flowOn(Dispatchers.IO)

    }

    override suspend fun getAllChatListingData(): Flow<List<ChatsListingModel>> {
        return flow { emit(localDataSource.getAllChatListingData().map { it.toDomain() }) }.flowOn(Dispatchers.IO)
    }

    override suspend fun saveToChatListing(ChatEntity: ChatsListingModel): Flow<String> {

        return flow { emit(localDataSource.saveChatListingData(ChatEntity.toEntity())) }.flowOn(Dispatchers.IO)

    }

    override suspend fun deleteChatListing(id: Int): Flow<Int> {
        return flow { emit(localDataSource.deleteChatListingData(id)) }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteChat(id: Int): Flow<Int> {

        return flow { emit(localDataSource.deleteChatData(id)) }.flowOn(Dispatchers.IO)
    }


}