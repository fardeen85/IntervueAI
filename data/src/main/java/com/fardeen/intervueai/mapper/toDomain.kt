package com.fardeen.intervueai.mapper

import com.fardeen.intervueai.local.ChatEntity
import com.fardeen.intevueai.model.ChatModel


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