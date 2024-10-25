package fun.destywen.houry;

import android.app.Application;

import androidx.room.Room;

import fun.destywen.houry.database.AppDatabase;

public class MyApplication extends Application {

    private static MyApplication mApp;
    private AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "destywen")
//                .addMigrations()
                .allowMainThreadQueries()
                .build();
    }

    public static MyApplication getInstance() {
        return mApp;
    }

    public AppDatabase getDB() {
        return appDatabase;
    }
}
