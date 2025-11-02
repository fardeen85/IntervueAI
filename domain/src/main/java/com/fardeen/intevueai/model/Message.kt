package com.fardeen.intevueai.model

data class Message(
    val content: String,
    val isFromAI: Boolean,
    val senderName: String = ""
)