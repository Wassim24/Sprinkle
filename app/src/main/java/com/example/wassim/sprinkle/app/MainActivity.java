package com.example.wassim.sprinkle.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wassim.sprinkle.R;
import com.example.wassim.sprinkle.adapters.AdapterPlants;
import com.example.wassim.sprinkle.beans.Plant;
import com.example.wassim.sprinkle.beans.PlantDao;
import com.example.wassim.sprinkle.extras.IntentKeys;
import com.example.wassim.sprinkle.factory.PlantFactory;
import com.example.wassim.sprinkle.widgets.GardenRecyclerView;

import java.util.List;

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

        setSupportActionBar(mToolbar);

        this.plantDao = new PlantFactory(getApplication()).getPlantDao();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.add_action:
                Intent intent = new Intent(MainActivity.this, AddPlant.class);
                intent.putExtra(IntentKeys.PLANT_NAME, "lol");
                startActivityForResult(intent, 202);
            break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_add:
                Plant lol = new Plant(null, "gege", 3);
                plantDao.insert(lol);
                List<Plant> plants = plantDao.loadAll();
                AdapterPlants adp = new AdapterPlants(this, plants);
                this.mRecyclerView.setAdapter(adp);
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 202)
        {
            switch (resultCode)
            {
                case RESULT_OK:
                    int wateringFrequency = data.getIntExtra(IntentKeys.PLANT_WATERING, 0);
                    String plantName = data.getStringExtra(IntentKeys.PLANT_NAME);
                    int picturePath = data.getIntExtra(IntentKeys.PLANT_PICTURE_PATH, -1);

                    if(wateringFrequency != 0 && !plantName.isEmpty() && picturePath == -1)
                    {
                        Plant lol = new Plant(null, plantName, wateringFrequency);
                        plantDao.insert(lol);
                    }
                    else if (wateringFrequency != 0 && !plantName.isEmpty() && picturePath != -1)
                    {
                        Plant lol = new Plant(null, plantName, wateringFrequency, picturePath);
                        plantDao.insert(lol);
                    }

                break;
            }
        }
    }
}
