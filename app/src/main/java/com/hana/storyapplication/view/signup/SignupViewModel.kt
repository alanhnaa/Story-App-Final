package com.hana.storyapplication.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hana.storyapplication.data.Result
import com.hana.storyapplication.data.UserRepository
import com.hana.storyapplication.data.response.RegisterResponse

class SignupViewModel(private var repository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> {
        return repository.register(name, email, password)
    }
}