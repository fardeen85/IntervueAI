package com.fardeen.intervueai.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "chat_table")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "message")
    var message: String = "",
    @ColumnInfo(name = "ownerMessage")
    var ownerMessage: String = "",
    @ColumnInfo(name = "chatId")
    var chatId: Int = 0
)
