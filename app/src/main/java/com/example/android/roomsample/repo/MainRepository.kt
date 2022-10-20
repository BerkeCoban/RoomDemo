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
package com.example.android.roomsample.repo

import androidx.annotation.WorkerThread
import com.example.android.roomsample.dao.MyDao
import com.example.android.roomsample.entities.Device
import com.example.android.roomsample.entities.User
import com.example.android.roomsample.entities.UserAndDevice
import kotlinx.coroutines.flow.Flow

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class MainRepository(private val MyDao: MyDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: Flow<List<User>> = MyDao.getAlphabetizedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    suspend fun insert(user: User) :Long {
       return MyDao.insert(user)
    }

    @WorkerThread
    suspend fun insertDevice(device: Device) {
        MyDao.insert(device)
    }

    @WorkerThread
    suspend fun update(user: User) {
        MyDao.update(user)
    }

    @WorkerThread
    suspend fun deleteUser(user: User) {
        MyDao.delete(user)
    }

    @WorkerThread
    suspend fun deleteDevice(device: Device) {
        MyDao.delete(device)
    }

    @WorkerThread
    suspend fun getById(id:Int) : User {
        return  MyDao.getUserById(id)
    }

    @WorkerThread
    suspend fun getByIdDevice(id:Int) : Device {
        return  MyDao.getDeviceById(id)
    }

    @WorkerThread
    suspend fun deleteAll() {
        MyDao.deleteAll()
        MyDao.deleteAll2()
    }


    @WorkerThread
    suspend fun getUserWithDevice(id:Int): List<UserAndDevice>? {
       return MyDao.getUsersAndDevices(id)
    }
}
