package com.fardeen.intervueai.local

class LocalDataSource(
    private val chatDao: ChatDao,
    private val chatListingDao: ChatListingDao
) {
    suspend fun getAllChatData(id: Int): List<ChatEntity> {
        return try {
            chatDao.getAllChatDataById(id)
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
            val rowId = chatListingDao.saveChatListingData(chats)
            return rowId.toString()?:"1"
        } catch (e: Exception) {
            e.printStackTrace()
            return e.message?.toString()?:""
        }
    }


    suspend fun deleteChatListingData(id: Int): Int {
        try {
            val rowsDeleted =  chatListingDao.deleteChatListingData(id)
            return  rowsDeleted
        } catch (e: Exception) {
            e.printStackTrace()
            return 0

        }
    }


    suspend fun deleteChatData(id: Int): Int{

        try {
          val rowsDeleted = chatDao.deleteChatData(id)
            return rowsDeleted
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }

}
