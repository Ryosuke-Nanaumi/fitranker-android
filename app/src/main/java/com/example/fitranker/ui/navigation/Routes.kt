package com.example.fitranker.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Ranking

@Serializable
data class History(val userId: Int)

@Serializable
object Login