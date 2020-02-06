package ru.deniskrd.android.webbrowser;

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.deniskrd.android.webbrowser.db.AppDatabase
import ru.deniskrd.android.webbrowser.db.entities.Favourite
import ru.deniskrd.android.webbrowser.db.entities.UserSetting
import java.util.*

class PropertiesHelper() {

    val userSettings = Properties()

    val favourites = Properties()

    private var appDatabase: AppDatabase? = null

    constructor(context: Context) : this() {
        appDatabase = AppDatabase.getInstance(context)
    }

    fun loadProperties() {
        appDatabase!!.queryExecutor.execute {
            appDatabase!!.userSettingDao().getAllUserSettings().forEach { userSetting ->
                userSettings.setProperty(userSetting.name, userSetting.value)
            }
            appDatabase!!.favouriteDao().getAllFavourites().forEach { favourite ->
                favourites.setProperty(favourite.name, favourite.url)
            }
        }
    }

    fun saveProperties() {
        appDatabase!!.transactionExecutor.execute {
            appDatabase!!.clearAllTables()

            userSettings.forEach { (name, value) ->
                appDatabase!!.userSettingDao().setUserSetting(UserSetting(name as String, value as String))
            }

            favourites.forEach { (name, value) ->
                appDatabase!!.favouriteDao().setFavourite(Favourite(name as String, value as String))
            }
        }
    }
}
