package com.fardeen.intervueai.mapper

import com.fardeen.intervueai.local.ChatEntity
import com.fardeen.intervueai.local.ChatListingEntity
import com.fardeen.intevueai.model.ChatModel
import com.fardeen.intevueai.model.ChatsListingModel


fun ChatEntity.toDomain() = ChatModel(
    id = id,
    message = message,
    messageOwner = ownerMessage
)

fun ChatModel.toEntity() = ChatEntity(
    id = id?:0,
    message = message?:"",
    ownerMessage = messageOwner?:""


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