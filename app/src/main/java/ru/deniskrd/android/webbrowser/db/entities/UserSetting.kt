package ru.deniskrd.android.webbrowser.db.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSetting (

    @PrimaryKey
    @NonNull
    val name : String = "",

    val value : String? = null
)
