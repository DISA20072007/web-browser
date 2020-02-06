package ru.deniskrd.android.webbrowser.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.deniskrd.android.webbrowser.db.dao.FavouriteDao
import ru.deniskrd.android.webbrowser.db.dao.UserSettingDao
import ru.deniskrd.android.webbrowser.db.entities.Favourite
import ru.deniskrd.android.webbrowser.db.entities.UserSetting

@Database(entities = [UserSetting::class, Favourite::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userSettingDao() : UserSettingDao
    abstract fun favouriteDao() : FavouriteDao

    companion object {
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database").build()
                }
            }
            return INSTANCE
        }
    }


}
