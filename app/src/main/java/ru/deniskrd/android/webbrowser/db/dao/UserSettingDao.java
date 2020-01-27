package ru.deniskrd.android.webbrowser.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.deniskrd.android.webbrowser.db.entities.UserSetting;

@Dao
public interface UserSettingDao {

    @Query("SELECT * FROM user_settings")
    List<UserSetting> getAllUserSettings();

    @Query("SELECT value FROM user_settings WHERE name = :name")
    String getSettingValue(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setUserSetting(UserSetting userSetting);
}
