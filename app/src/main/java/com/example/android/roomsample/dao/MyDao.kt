/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.roomsample.dao

import androidx.room.*
import com.example.android.roomsample.entities.Device
import com.example.android.roomsample.entities.User
import com.example.android.roomsample.entities.UserAndDevice
import kotlinx.coroutines.flow.Flow

/**
 * The Room Magic is in this file, where you map a method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

   @Dao
   interface MyDao : BaseDao<Device> {


    @Query("SELECT * FROM user_table ORDER BY name ASC")
    fun getAlphabetizedWords(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User) : Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("DELETE FROM device_table")
    suspend fun deleteAll2()

    @Query("SELECT * FROM user_table WHERE id=:id")
    suspend fun getUserById(id: Int) : User

    @Query("SELECT * FROM device_table WHERE id=:id")
    suspend fun getDeviceById(id: Int) : Device

   @Delete
   suspend fun delete(user: User)


    @Transaction
    @Query("SELECT * FROM user_table WHERE id=:id")
   suspend fun getUsersAndDevices(id: Int): List<UserAndDevice>?




}
