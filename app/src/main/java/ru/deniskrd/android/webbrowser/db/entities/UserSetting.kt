package ru.deniskrd.android.webbrowser.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(tableName = "user_settings")
@NoArgsConstructor
@AllArgsConstructor
class UserSetting(name: String, value : String) {

    enum class Settings { HOME_PAGE }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    val name : String? = null

    @ColumnInfo(name = "value")
    val value : String? = null
}
