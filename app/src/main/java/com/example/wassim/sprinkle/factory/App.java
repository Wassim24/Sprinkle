package com.example.wassim.sprinkle.factory;

import android.app.Application;

import com.example.wassim.sprinkle.beans.DaoMaster;
import com.example.wassim.sprinkle.beans.DaoMaster.DevOpenHelper;
import com.example.wassim.sprinkle.beans.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by wassim on 11/19/16.
 */

public class App extends Application
{

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DevOpenHelper helper = new DevOpenHelper(this, "plantes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession()
    {
        return this.daoSession;
    }
}
