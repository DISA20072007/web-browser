package ru.deniskrd.android.webbrowser.db.dao;

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.deniskrd.android.webbrowser.db.entities.Favourite

@Dao
interface FavouriteDao {

    @Query("SELECT * FROM favourites")
    fun getAllFavourites() : MutableList<Favourite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFavourite(favourite: Favourite)
}
