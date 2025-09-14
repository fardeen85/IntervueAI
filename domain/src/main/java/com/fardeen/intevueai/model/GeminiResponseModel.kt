package com.fardeen.intevueai.model

import kotlinx.serialization.Serializable


@Serializable
data class GeminiResponseModel(
    val candidates: List<Candidate>,
    val usageMetadata: UsageMetadata,
    val modelVersion: String,
    val responseId: String
)

@Serializable
data class Candidate(
    val content: Content,
    val finishReason: String,
    val avgLogprobs: Double
)

@Serializable
data class Content(
    val parts: List<Part>,
    val role: String
)

@Serializable
data class Part(
    val text: String
)

@Serializable
data class UsageMetadata(
    val promptTokenCount: Int,
    val candidatesTokenCount: Int,
    val totalTokenCount: Int,
    val promptTokensDetails: List<TokenDetail>,
    val candidatesTokensDetails: List<TokenDetail>
)

@Serializable
data class TokenDetail(
    val modality: String,
    val tokenCount: Int
)
