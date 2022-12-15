package com.jiemaibj.metro.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    var name: String,
    var description: String,
    var image: String? = null,
)