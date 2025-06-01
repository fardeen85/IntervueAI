package com.fardeen.intevueai.model

data class GeminiResponseModel(
    val candidates: List<Candidate>,
    val usageMetadata: UsageMetadata,
    val modelVersion: String,
    val responseId: String
)

data class Candidate(
    val content: Content,
    val finishReason: String,
    val avgLogprobs: Double
)

data class Content(
    val parts: List<Part>,
    val role: String
)

data class Part(
    val text: String
)

data class UsageMetadata(
    val promptTokenCount: Int,
    val candidatesTokenCount: Int,
    val totalTokenCount: Int,
    val promptTokensDetails: List<TokenDetail>,
    val candidatesTokensDetails: List<TokenDetail>
)

data class TokenDetail(
    val modality: String,
    val tokenCount: Int
)
