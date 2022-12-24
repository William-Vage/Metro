package com.jiemaibj.metro.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoggedInUser(
    val userId: String,
    val username: String,
    val permissions: List<Int> = emptyList(),
    val secret: String,
)