package com.hana.storyapplication.data.preference

data class UserModel(
val token: String,
val name: String,
val userId: String,
val isLogin: Boolean = false
)