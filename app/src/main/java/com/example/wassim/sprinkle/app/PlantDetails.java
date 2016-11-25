package com.example.wassim.sprinkle.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wassim.sprinkle.R;
import com.example.wassim.sprinkle.beans.Plant;
import com.example.wassim.sprinkle.beans.PlantDao;
import com.example.wassim.sprinkle.extras.DateUtil;
import com.example.wassim.sprinkle.extras.IntentKeys;
import com.example.wassim.sprinkle.factory.PlantFactory;

import java.io.File;

public class PlantDetails extends AppCompatActivity implements View.OnClickListener{

    // Les différents composants du layout
    TextView mPlantName;
    TextView mPlantDate;
    TextView mPreviousWater;
    TextView mNextWater;
    TextView mWaterFrequency;

    // Attributs permettant de charger la photo d'une plante
    String mPicturePath;
    long mPlantId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        // Initialisation de la toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Détails de la plante");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Gestion des cliques sur le floatingactionbutton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        // initialisation des détails de la plante, et caching du background
        initPlantDetails();
        initBackgroundImage();
    }

    /**
     * Initialisation des menus de la toolbar
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_edit_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Gestion du cliques sur le bouton d'édition sur la toolbar
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId()) {
            case R.id.edit_action:
                Intent edit = new Intent(this, AddPlant.class);
                edit.putExtra(IntentKeys.PLANT_ID, this.mPlantId);
                edit.putExtra(IntentKeys.PLANT_NAME, this.mPlantName.getText());
                edit.putExtra(IntentKeys.PLANT_WATERING_FQ, Integer.valueOf(this.mWaterFrequency.getText().toString()));
                edit.putExtra(IntentKeys.PLANT_PICTURE_PATH, this.mPicturePath);
                startActivityForResult(edit, 202);
            break;

            case android.R.id.home:
                onBackPressed();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode permettant d'initialiser les différentes elements de la vue
     */
    private void initPlantDetails()
    {

        // Initialisation des différents elements du layout
        this.mPlantName = (TextView) findViewById(R.id.detail_plant_name);
        this.mPlantDate = (TextView) findViewById(R.id.detail_plant_date);
        this.mPreviousWater = (TextView) findViewById(R.id.detail_previous_water);
        this.mNextWater = (TextView) findViewById(R.id.detail_next_water);
        this.mWaterFrequency = (TextView) findViewById(R.id.detail_water_fq);

        Intent details = getIntent();

        this.mPlantName.setText(details.getStringExtra(IntentKeys.PLANT_NAME));
        this.mWaterFrequency.setText(String.valueOf(details.getIntExtra(IntentKeys.PLANT_WATERING_FQ, 0)));

        this.mPreviousWater.setText(DateUtil.formatDate(details.getLongExtra(IntentKeys.PLANT_PREVIOUS_WATERING, 0)));
        this.mPlantDate.setText(DateUtil.formatDate(details.getLongExtra(IntentKeys.PLANT_DATE, 0)));
        this.mNextWater.setText(DateUtil.formatDate(details.getLongExtra(IntentKeys.PLANT_NEXT_WATERING, 0)));
        this.mPlantId = details.getLongExtra(IntentKeys.PLANT_ID, -1);

        // On recupère le path de l'image
        this.mPicturePath = Environment.DIRECTORY_PICTURES +"/Sprinkle/" + details.getStringExtra(IntentKeys.PLANT_PICTURE_PATH);
    }

    /**
     * Méthode permettant de faire le caching de l'image de fond
     */
    private void initBackgroundImage()
    {
        ImageView background = (ImageView) findViewById(R.id.plant_photo);
        Glide.with(this)
                .load(new File(String.valueOf(Environment.getExternalStoragePublicDirectory(this.mPicturePath))))
                .centerCrop()
                .into(background);
    }

    /**
     * Permet de gérer la mise à jour des informations de la plante après édition
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 202)
        {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // On recupère les informations renvoyés par l'acitivité d'edition
                    int wateringFrequency = data.getIntExtra(IntentKeys.PLANT_WATERING_FQ, 0);
                    String plantName = data.getStringExtra(IntentKeys.PLANT_NAME);
                    String picturePath = (data.getStringExtra(IntentKeys.PLANT_PICTURE_PATH) == null) ? "" : data.getStringExtra(IntentKeys.PLANT_PICTURE_PATH);
                    long plantId = data.getLongExtra(IntentKeys.PLANT_ID, -1);

                    // On vérifie l'ID
                    if(plantId != -1)
                    {
                        // On récupére l'instance de la plante à modifier
                        PlantDao plantDao = PlantFactory.getInstance().getPlantDao();
                        Plant plant = plantDao.loadByRowId(plantId);

                        // Deux cas se présentent, l'utilisateur a changé de photo ou pas
                        if(wateringFrequency != 0 && !plantName.isEmpty() && picturePath.isEmpty())
                        {
                            plant.setName(plantName);
                            plant.setWateringFrequency(wateringFrequency);
                            plantDao.update(plant);
                        }
                        else if (wateringFrequency != 0 && !plantName.isEmpty() && !picturePath.isEmpty())
                        {
                            plant.setName(plantName);
                            plant.setWateringFrequency(wateringFrequency);
                            plant.setPicturePath(picturePath);
                            plantDao.update(plant);
                        }
                    }
                break;

                case Activity.RESULT_CANCELED:
                break;
            }

        }
    }

    /**
     * Gestion du clique lors de l'appuie sur le floatingactionbutton pour la mise à jour de l'arrosage
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        // On vérifie que l'id est bien présent
        if(this.mPlantId != -1)
        {
            // ON recupere une instance de la plante que l'on veut éditer et on change les dates d'arrosage
            Plant plant = PlantFactory.getInstance().getPlantDao().loadByRowId(this.mPlantId);
            plant.setLastWateringAt(System.currentTimeMillis());
            PlantFactory.getInstance().getPlantDao().update(plant);
            this.mPreviousWater.setText(DateUtil.formatDate(plant.getLastWateringAt()));
            plant = null;
        }
    }

    /**
     * Gestion du clique sur le bouton retour
     */
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
