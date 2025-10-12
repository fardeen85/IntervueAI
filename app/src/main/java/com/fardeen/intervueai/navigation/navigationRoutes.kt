package com.fardeen.intervueai.navigation

import kotlinx.serialization.Serializable

interface NavigationRoutes

@Serializable
object Home : NavigationRoutes
object SelectTopic:NavigationRoutes

object createChatMeta:NavigationRoutes

object chatScreen : NavigationRoutes
