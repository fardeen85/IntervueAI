package com.fardeen.intervueai.local

class LocalDataSource(
    private val chatDao: ChatDao,
    private val chatListingDao: ChatListingDao
) {
    suspend fun getAllChatData(): List<ChatEntity> {
        return try {
            chatDao.getAllChatData()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // or rethrow depending on your policy
        }
    }

    suspend fun saveChatData(chats: ChatEntity): String {
        try {
            chatDao.saveChatData(chats)
            return "success"
        } catch (e: Exception) {
            e.printStackTrace()
            return e.message?.toString()?:""
        }
    }

    suspend fun getAllChatListingData(): List<ChatListingEntity> {

        return try {
            chatListingDao.getAllChatData()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // or rethrow depending on your policy
        }

    }


    suspend fun saveChatListingData(chats: ChatListingEntity): String {
        try {
            chatListingDao.saveChatListingData(chats)
            return "success"
        } catch (e: Exception) {
            e.printStackTrace()
            return e.message?.toString()?:""
        }
    }

}
