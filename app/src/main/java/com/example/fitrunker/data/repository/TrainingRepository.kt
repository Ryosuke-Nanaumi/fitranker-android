package com.example.fitrunker.data.repository

import com.example.fitrunker.data.remote.ApiClient
import com.example.fitrunker.data.remote.PersonalUserInfo
import com.example.fitrunker.data.remote.TrainingRecordResponse
import com.example.fitrunker.data.remote.RankingInfo
import com.example.fitrunker.data.remote.TrainingApi
import com.example.fitrunker.data.remote.TrainingRecordRequest

class TrainingRepository(
    private val api: TrainingApi = ApiClient.create(TrainingApi::class.java)
) {
    suspend fun getPersonalInfo(userId: Int): PersonalUserInfo {
        return api.getPersonalInfo(userId)
    }

    suspend fun getRankingInfo(): List<RankingInfo> {
        return api.getRankingInfo()
    }

    suspend fun postTrainingRecord(input: TrainingRecordRequest): TrainingRecordResponse {
        return api.postTrainingRecord(input)
    }
}