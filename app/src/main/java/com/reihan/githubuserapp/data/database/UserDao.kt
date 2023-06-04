package com.reihan.githubuserapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("SELECT * from UserGithub ORDER BY username ASC")
    fun getAllFavoriteData(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserGithub WHERE username = :username")
    fun getDataByUsername(username: String): LiveData<List<UserEntity>>
}