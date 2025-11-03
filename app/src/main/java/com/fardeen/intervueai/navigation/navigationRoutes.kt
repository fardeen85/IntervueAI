package com.fardeen.intervueai.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
object Home : NavKey

@Serializable
object SelectTopic:NavKey

@Serializable
object createChatMeta:NavKey

@Serializable
data class chatScreen(val chatId: String? = null) : NavKey
