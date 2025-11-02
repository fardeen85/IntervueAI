package com.fardeen.intervueai.mapper


import com.fardeen.intervueai.local.ChatEntity
import com.fardeen.intervueai.local.ChatListingEntity
import com.fardeen.intevueai.model.ChatModel
import com.fardeen.intevueai.model.ChatsListingModel
import com.fardeen.intevueai.model.Message




fun ChatEntity.toMessage()= Message(
    content = message,
    isFromAI =  if(ownerMessage == "AI") true else false,
    senderName = ownerMessage


)


fun ChatModel.toEntity() = ChatEntity(
    id = id?:0,
    message = message?:"",
    ownerMessage = messageOwner?:"",
    chatId = chatId?:1



)

fun ChatListingEntity.toDomain() = ChatsListingModel(

    id = id,
    title = title,
    description = description

)




fun ChatsListingModel.toEntity() = ChatListingEntity(

    id = id?:0,
    title = title?:"",
    description = description?:""
)