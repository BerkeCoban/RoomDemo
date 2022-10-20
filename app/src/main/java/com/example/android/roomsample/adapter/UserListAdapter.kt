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

package com.example.android.roomsample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.roomsample.R
import com.example.android.roomsample.ui.WordViewModel
import com.example.android.roomsample.adapter.UserListAdapter.WordViewHolder
import com.example.android.roomsample.entities.User

class UserListAdapter(vm : WordViewModel) : ListAdapter<User, WordViewHolder>(WORDS_COMPARATOR) {

    var  viewModel : WordViewModel = vm

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name,current.id.toString(),current.surname,viewModel)
    }

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.textView)
        private val id: TextView = itemView.findViewById(R.id.textView2)
        private val surname: TextView = itemView.findViewById(R.id.textView3)
        private val layout: ConstraintLayout = itemView.findViewById(R.id.linear)
        private val delete: Button = itemView.findViewById(R.id.delete)

        fun bind(text: String?,text2: String?,text3:String?,vm: WordViewModel) {
            wordItemView.text = text
            id.text = text2
            surname.text = text3
            layout.setOnClickListener {
            vm.getUserAndDevice(Integer.valueOf(id.text.toString()))
            }

            layout.setOnLongClickListener {
               // vm.updateById(id.text.toString().toInt(),"cobi")
                vm.update(id.text.toString().toInt())
                return@setOnLongClickListener true
            }

            delete.setOnClickListener {
                vm.delete(id.text.toString().toInt())
            }
        }

        companion object {
            fun create(parent: ViewGroup): WordViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return WordViewHolder(view)
            }
        }
    }

    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}
