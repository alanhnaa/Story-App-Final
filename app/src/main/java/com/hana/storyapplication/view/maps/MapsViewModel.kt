package com.hana.storyapplication.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hana.storyapplication.data.Result
import com.hana.storyapplication.data.UserRepository
import com.hana.storyapplication.data.response.StoryResponse

class MapsViewModel(
    private val repository: UserRepository
) : ViewModel() {

    fun getStoriesWithLocation(): LiveData<Result<StoryResponse>> {
        return repository.getStoriesWithLocation()
    }

}