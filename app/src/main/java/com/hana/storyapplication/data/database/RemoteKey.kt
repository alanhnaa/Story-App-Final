package com.hana.storyapplication.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key_story")
data class RemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)