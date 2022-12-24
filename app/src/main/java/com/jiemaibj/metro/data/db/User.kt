package com.jiemaibj.metro.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User(
    @PrimaryKey val id: String,
    val username: String,
    val realName: String,
    val key: String,
    val secret: String,
    val lastDate: Calendar = Calendar.getInstance(),
)