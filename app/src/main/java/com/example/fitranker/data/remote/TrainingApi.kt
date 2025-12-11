package com.example.fitranker.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.DELETE
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

data class TrainingRecordInfo(
    val exerciseId: Int,
    val date: String,
    val amount: Int,
    val point: Int,
    val trainingId: Int,
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

    @GET("api/training-records/{id}")
    suspend fun getTrainingRecords(
        @Path("id") id: Int
    ): List<TrainingRecordInfo>

    @POST("api/training-records")
    suspend fun postTrainingRecord(
        @Body body: TrainingRecordRequest
    ): TrainingRecordResponse

    @DELETE("api/training-records/{id}")
    suspend fun deleteTrainingRecord(
        @Path("id") id: Int
    )
}