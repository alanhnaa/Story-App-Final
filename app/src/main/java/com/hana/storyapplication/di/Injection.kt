package com.hana.storyapplication.di

import android.content.Context
import com.hana.storyapplication.data.UserRepository
import com.hana.storyapplication.data.preference.UserPreference
import com.hana.storyapplication.data.database.StoryDatabase
import com.hana.storyapplication.data.preference.dataStore
import com.hana.storyapplication.data.remote.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun injectionRepository(context: Context): UserRepository = runBlocking  {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = pref.getSession().first()
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoryDatabase.getDatabase(context)
        UserRepository.getInstance(database, apiService, pref)
    }
}