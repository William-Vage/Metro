package com.jiemaibj.metro.data.model

data class LoggedInUser(
    val userId: String,
    val username: String,
    val permissions: List<Int> = emptyList()
)