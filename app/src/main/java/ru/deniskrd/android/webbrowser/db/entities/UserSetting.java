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
public class UserSetting {

    public enum Settings { HOME_PAGE }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "value")
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
