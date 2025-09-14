package com.fardeen.intervueai.local

class LocalDataSource(
    private val chatDao: ChatDao
) {
    suspend fun getAllChatData(): List<ChatEntity> {
        return try {
            chatDao.getAllChatData()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // or rethrow depending on your policy
        }
    }

    suspend fun saveChatData(chats: List<ChatEntity>) {
        try {
            chatDao.saveChatData(chats)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
