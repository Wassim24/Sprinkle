package com.example.wassim.sprinkle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wassim.sprinkle.adapters.AdapterPlants;
import com.example.wassim.sprinkle.beans.DaoSession;
import com.example.wassim.sprinkle.beans.Plant;
import com.example.wassim.sprinkle.beans.PlantDao;
import com.example.wassim.sprinkle.factory.App;
import com.example.wassim.sprinkle.widgets.GardenRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    GardenRecyclerView mRecyclerView;
    AdapterPlants mPlantsAdapter;
    View mEmptyPlants;
    Toolbar mToolbar;


    PlantDao plantDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add = (Button) findViewById(R.id.btn_add);
        add.setOnClickListener(this);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mEmptyPlants = findViewById(R.id.empty_plants);

        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        plantDao = daoSession.getPlantDao();
        plantDao.deleteAll();

        mRecyclerView = (GardenRecyclerView) findViewById(R.id.plant_list);
        mRecyclerView.hideIfNoPlants(mToolbar);
        mRecyclerView.showIfPlants(mEmptyPlants);
        mPlantsAdapter = new AdapterPlants(this, plantDao.loadAll());
        mRecyclerView.setAdapter(mPlantsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Caching du background
        initBackgroundImage();
    }

    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.app_bg);
        Glide.with(this)
                .load(R.drawable.lnd)
                .centerCrop()
                .into(background);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_add:
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, 10);

                Toast.makeText(this, df.format(new Date().getTime()), Toast.LENGTH_LONG).show();
                Toast.makeText(this, df.format(c.getTime()), Toast.LENGTH_LONG).show();
                plantDao.insert(new Plant(null, "geranium", 12));
                mPlantsAdapter.setmItem(plantDao.loadAll());
                mPlantsAdapter.notifyDataSetChanged();
            break;
        }
    }
}
