package ru.deniskrd.android.webbrowser;

import android.content.Context;

import java.util.Properties;

import ru.deniskrd.android.webbrowser.db.AppDatabase;
import ru.deniskrd.android.webbrowser.db.entities.Favourite;
import ru.deniskrd.android.webbrowser.db.entities.UserSetting;

class PropertiesHelper {

    private Properties userSettings;
    private Properties favourites;

    private AppDatabase appDatabase;

    PropertiesHelper(Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }

    Properties getUserSettings() {
        return userSettings;
    }

    Properties getFavourites() {
        return favourites;
    }

    void loadProperties() {
        userSettings = new Properties();
        favourites = new Properties();

        appDatabase.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                for (UserSetting userSetting : appDatabase.userSettingDao().getAllUserSettings()) {
                    userSettings.setProperty(userSetting.getName(), userSetting.getValue());
                }

                for (Favourite favourite : appDatabase.favouriteDao().getAllFavourites()) {
                    favourites.setProperty(favourite.getName(), favourite.getUrl());
                }
            }
        });
    }

    void saveProperties() {
        appDatabase.getTransactionExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.clearAllTables();

                for (String settingName : userSettings.stringPropertyNames()) {
                    appDatabase.userSettingDao().setUserSetting(new UserSetting(settingName, userSettings.getProperty(settingName)));
                }

                for (String favouriteName : favourites.stringPropertyNames()) {
                    appDatabase.favouriteDao().setFavourite(new Favourite(favouriteName, favourites.getProperty(favouriteName)));
                }
            }
        });
    }
}
