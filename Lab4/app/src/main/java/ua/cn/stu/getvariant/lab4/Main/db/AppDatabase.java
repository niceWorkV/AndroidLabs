package ua.cn.stu.getvariant.lab4.Main.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BookDbEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BookDAO getBookDAO();

}
