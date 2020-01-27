package ru.deniskrd.android.webbrowser.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.deniskrd.android.webbrowser.db.entities.Favourite;

@Dao
public interface FavouriteDao {

    @Query("SELECT * FROM favourites")
    List<Favourite> getAllFavourites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setFavourite(Favourite favourite);
}
