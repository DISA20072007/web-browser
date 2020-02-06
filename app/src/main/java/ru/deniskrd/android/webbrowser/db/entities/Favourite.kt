package ru.deniskrd.android.webbrowser.db.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class Favourite (

    @PrimaryKey
    @NonNull
    val name : String = "",

    val url : String? = null
)
