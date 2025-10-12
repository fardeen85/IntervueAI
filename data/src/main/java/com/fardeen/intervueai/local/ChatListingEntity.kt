package com.fardeen.intervueai.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_listing_table")
data class ChatListingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String
)