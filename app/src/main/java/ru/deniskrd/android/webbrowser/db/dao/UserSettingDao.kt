package ru.deniskrd.android.webbrowser.db.dao;

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.deniskrd.android.webbrowser.db.entities.UserSetting

@Dao
interface UserSettingDao {

    @Query("SELECT * FROM user_settings")
    fun getAllUserSettings() : MutableList<UserSetting>

    @Query("SELECT value FROM user_settings WHERE name = :name")
    fun getSettingValue(name :String) : String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setUserSetting(userSetting: UserSetting)
}
