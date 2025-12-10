package com.example.fitrunker.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

//@JsonClass(generateAdapter = true)
data class PersonalUserInfo(
    val userId: Int,
    val name: String,
    val totalPoint: Int,
    val todaysPoint: Int,
)

//@JsonClass(generateAdapter = true)
data class RankingInfo(
    val id: Int,
    val name: String,
    val point: Int,
)

//@JsonClass(generateAdapter = true)
data class TrainingRecordRequest(
    val userId: Int,
    val exerciseId: Int,
    val date: String,
    val amount: Int,
)

//@JsonClass(generateAdapter = true)
data class TrainingRecordResponse(
    val id: Int
)

interface TrainingApi {
    @GET("api/personal/{id}")
    suspend fun getPersonalInfo(
        @Path("id") id: Int
    ): PersonalUserInfo

    @GET("api/ranking")
    suspend fun getRankingInfo(): List<RankingInfo>

    @POST("api/training_records")
    suspend fun postTrainingRecord(
        @Body body: TrainingRecordRequest
    ): TrainingRecordResponse
}