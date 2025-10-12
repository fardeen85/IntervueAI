package com.fardeen.intervueai.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChatListingDao{

    @Query("SELECT * FROM chat_listing_table")
    suspend fun getAllChatData(): List<ChatListingEntity>

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun saveChatListingData(chatEntity: ChatListingEntity)




}