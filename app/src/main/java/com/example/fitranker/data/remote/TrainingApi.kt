package com.example.fitranker.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

//@JsonClass(generateAdapter = true)
data class PersonalUserInfo(
    @param:Json(name = "id")
    val userId: Int,
    val name: String,
    @param:Json(name = "totalPoints")
    val totalPoint: Int,
    val todaysPoint: Int,
)

//@JsonClass(generateAdapter = true)
data class RankingInfo(
    val id: Int,
    val name: String,
    val point: Int,
)

@JsonClass(generateAdapter = true)
data class TrainingRecordRequest(
    @param:Json(name = "userId")
    val userId: Int,
    val exerciseId: Int,
    val date: String,
    val amount: Int,
)

@JsonClass(generateAdapter = true)
data class TrainingRecordResponse(
    @param:Json(name = "createdId")
    val id: Int
)

interface TrainingApi {
    @GET("api/personal/{id}")
    suspend fun getPersonalInfo(
        @Path("id") id: Int
    ): PersonalUserInfo

    @GET("api/ranking")
    suspend fun getRankingInfo(): List<RankingInfo>

    @POST("api/training-records")
    suspend fun postTrainingRecord(
        @Body body: TrainingRecordRequest
    ): TrainingRecordResponse
}