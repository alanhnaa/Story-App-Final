package com.hana.storyapplication.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hana.storyapplication.data.UserRepository
import com.hana.storyapplication.data.preference.UserModel
import com.hana.storyapplication.data.response.ListStoryItem

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    val getStory: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun logout() {
        repository.logout()
    }

}