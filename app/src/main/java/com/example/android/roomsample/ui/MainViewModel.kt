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

package com.example.android.roomsample.ui

import android.os.Build
import androidx.lifecycle.*
import com.example.android.roomsample.entities.Device
import com.example.android.roomsample.entities.User
import com.example.android.roomsample.entities.UserAndDevice
import com.example.android.roomsample.repo.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class WordViewModel(private val repository: MainRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<User>> = repository.allWords.asLiveData()

    val deviceInfo: MutableLiveData<List<UserAndDevice>?> = MutableLiveData()

    val insertedUser: MutableLiveData<Long> = MutableLiveData()

    val updateSignal: MutableLiveData<Int> = MutableLiveData()

    fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        insertedUser.postValue(repository.insert(user))
    }

    fun delete(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteUser(repository.getById(id))
        repository.deleteDevice(repository.getByIdDevice(id))
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun update(id:Int) = viewModelScope.launch(Dispatchers.IO) {
        updateSignal.postValue(id)
    }


    // called on click to update
    fun updateById(id: Int, name: String,surName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user: User = repository.getById(id)
            user.name = name
            user.surname = surName
            repository.update(user)
        }
    }


    fun insertDevice(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertDevice(Device(id, getDeviceName()))
    }

    fun getUserAndDevice(id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deviceInfo.postValue(repository.getUserWithDevice(id))
        }
    }

}

class WordViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
        capitalize(model)
    } else {
        capitalize(manufacturer) + " " + model
    }
}


private fun capitalize(s: String?): String {
    if (s == null || s.isEmpty()) {
        return ""
    }
    val first = s[0]
    return if (Character.isUpperCase(first)) {
        s
    } else {
        Character.toUpperCase(first).toString() + s.substring(1)
    }
}
