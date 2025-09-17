package com.fardeen.intervueai.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChatDao{

    @Query("SELECT * FROM chat_table")
    suspend fun getAllChatData(): List<ChatEntity>

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun saveChatData(chatEntity: ChatEntity)

}