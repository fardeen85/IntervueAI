package com.fardeen.intevueai.model

import kotlinx.serialization.Serializable


@Serializable
data class GeminiRequest(
    val contents: List<RequestContent>
)

@Serializable
data class RequestContent(
    val parts: List<RequestPart>,
)

@Serializable
data class RequestPart(
    val text: String
)
