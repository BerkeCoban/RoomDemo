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

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.roomsample.*
import com.example.android.roomsample.adapter.UserListAdapter
import com.example.android.roomsample.databinding.ActivityMainBinding
import com.example.android.roomsample.entities.User


class MainActivity : AppCompatActivity() {

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as MyApplication).repository)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = UserListAdapter(wordViewModel)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val word =   User(result.data?.getStringExtra(NewUserActivity.EXTRA_REPLY).toString(),result.data?.getStringExtra(NewUserActivity.EXTRA_REPLY_SURNAME).toString())
                wordViewModel.insert(word)
            } else {
                Toast.makeText(
                        applicationContext,
                       "empty fields.",
                        Toast.LENGTH_LONG
                ).show()
            }
        }


        binding.fab.setOnClickListener {
            startForResult.launch(Intent(this@MainActivity, NewUserActivity::class.java))
        }
        binding.remove.setOnClickListener {
        wordViewModel.deleteAll()
        }


        wordViewModel.allWords.observe(this) { words ->
            // Update the cached copy of the words in the adapter.
            words.let { adapter.submitList(it) }
        }

        wordViewModel.deviceInfo.observe(this){

            val ad = AlertDialog.Builder(this).create()
            ad.setMessage(it.toString())
            ad.setCancelable(true)
            ad.show()
        }

        wordViewModel.insertedUser.observe(this){
            wordViewModel.insertDevice(it.toInt())
        }

        wordViewModel.updateSignal.observe(this) {


            val view: View = layoutInflater.inflate(R.layout.alert_dialog, null)
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Update User")
            alertDialog.setCancelable(true)

            val updatedName = view.findViewById(R.id.names) as EditText
            val updatedSurname = view.findViewById(R.id.surnames) as EditText

            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                    "OK") { _, _ ->
                wordViewModel.updateById(id = it, name = updatedName.text.toString(), surName = updatedSurname.text.toString())
            }

            alertDialog.setView(view);
            alertDialog.show();
        }


    }
}
