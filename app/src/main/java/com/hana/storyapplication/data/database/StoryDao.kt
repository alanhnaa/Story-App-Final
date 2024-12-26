package com.hana.storyapplication.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hana.storyapplication.data.response.ListStoryItem

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(story: List<ListStoryItem>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM story")
    fun deleteAllStory()
}