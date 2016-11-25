package com.example.wassim.sprinkle.factory;

import android.app.Application;

import com.example.wassim.sprinkle.beans.DaoSession;
import com.example.wassim.sprinkle.beans.PlantDao;

/**
 *  Singleton permettant d'avoir accés à la DAO pour gérer l'entité Plant
 */
public class PlantFactory {

    private static PlantFactory ourInstance;
    private PlantDao plantDao;

    public static PlantFactory getInstance() {
        return ourInstance;
    }

    public PlantFactory(Application app) {
        ourInstance = this;
        DaoSession daoSession = ((App) app).getDaoSession();
        plantDao = daoSession.getPlantDao();
    }

    public PlantDao getPlantDao()
    {return this.plantDao;}
}
