package com.example.fitranker.data.repository

import com.example.fitranker.data.remote.ApiClient
import com.example.fitranker.data.remote.PersonalUserInfo
import com.example.fitranker.data.remote.TrainingRecordResponse
import com.example.fitranker.data.remote.RankingInfo
import com.example.fitranker.data.remote.TrainingApi
import com.example.fitranker.data.remote.TrainingRecordInfo
import com.example.fitranker.data.remote.TrainingRecordRequest

class TrainingRepository(
    private val api: TrainingApi = ApiClient.create(TrainingApi::class.java)
) {
    suspend fun getPersonalInfo(userId: Int): PersonalUserInfo {
        return api.getPersonalInfo(userId)
    }

    suspend fun getRankingInfo(): List<RankingInfo> {
        return api.getRankingInfo()
    }

    suspend fun getTrainingRecords(userId: Int): List<TrainingRecordInfo> {
        return api.getTrainingRecords(userId)
    }

    suspend fun postTrainingRecord(input: TrainingRecordRequest): TrainingRecordResponse {
        return api.postTrainingRecord(input)
    }
}