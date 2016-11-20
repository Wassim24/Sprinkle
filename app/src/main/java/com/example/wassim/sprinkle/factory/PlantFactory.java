package com.example.wassim.sprinkle.factory;

import android.app.Application;

import com.example.wassim.sprinkle.beans.DaoSession;
import com.example.wassim.sprinkle.beans.PlantDao;

/**
 * Created by wassim on 11/20/16.
 */
public class PlantFactory {

    private static PlantFactory ourInstance;
    private PlantDao plantDao;


    public static PlantFactory getInstance() {
        return ourInstance;
    }

    public PlantFactory() {}

    public PlantFactory(Application app) {
        ourInstance = this;
        DaoSession daoSession = ((App) app).getDaoSession();
        plantDao = daoSession.getPlantDao();
    }

    public PlantDao getPlantDao()
    {return this.plantDao;}
}
