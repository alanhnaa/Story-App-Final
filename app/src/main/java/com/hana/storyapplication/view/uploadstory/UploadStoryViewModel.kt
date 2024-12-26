package com.hana.storyapplication.view.uploadstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hana.storyapplication.data.Result
import com.hana.storyapplication.data.UserRepository
import com.hana.storyapplication.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(
    private val repository: UserRepository
) : ViewModel() {

    fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        long: Float
    ): LiveData<Result<UploadResponse>> {
        return repository.uploadImage(file, description, lat, long)
    }
}