package ru.deniskrd.android.webbrowser.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(tableName = "favourites")
@NoArgsConstructor
@AllArgsConstructor
class Favourite(name : String, url : String) {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    val name : String? = null

    @ColumnInfo(name = "url")
    val url : String? = null
}
