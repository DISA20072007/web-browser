package ru.deniskrd.android.webbrowser.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.deniskrd.android.webbrowser.db.dao.FavouriteDao;
import ru.deniskrd.android.webbrowser.db.dao.UserSettingDao;
import ru.deniskrd.android.webbrowser.db.entities.Favourite;
import ru.deniskrd.android.webbrowser.db.entities.UserSetting;

@Database(entities = {UserSetting.class, Favourite.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserSettingDao userSettingDao();
    public abstract FavouriteDao favouriteDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "app_database").build();
        }
        return INSTANCE;
    }
}
