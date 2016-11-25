package com.example.wassim.sprinkle.factory;

import android.app.Application;

import com.example.wassim.sprinkle.beans.DaoMaster;
import com.example.wassim.sprinkle.beans.DaoMaster.DevOpenHelper;
import com.example.wassim.sprinkle.beans.DaoSession;

import org.greenrobot.greendao.database.Database;

public class App extends Application
{
    private DaoSession daoSession;

    // Initialisation d'une session pour GREENDAO
    @Override
    public void onCreate() {
        super.onCreate();

        // Creation de la BDD et initialisation de la session pour les accés à la BDD
        DevOpenHelper helper = new DevOpenHelper(this, "plantes-db");
        Database db = helper.getWritableDb();
        DaoMaster dm = new DaoMaster(db);
        daoSession = dm.newSession();
    }

    // Getter pour la session DAO
    public DaoSession getDaoSession()
    {
        return this.daoSession;
    }
}
