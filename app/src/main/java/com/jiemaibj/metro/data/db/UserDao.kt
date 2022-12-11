package com.jiemaibj.metro.data.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface UserDao {

    @Insert
   suspend fun insert(user: User)
}