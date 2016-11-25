package com.example.wassim.sprinkle.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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
import com.example.wassim.sprinkle.extras.FileUtil;
import com.example.wassim.sprinkle.extras.IntentKeys;
import com.example.wassim.sprinkle.factory.PlantFactory;
import com.example.wassim.sprinkle.widgets.DatePicker;
import com.example.wassim.sprinkle.widgets.GardenRecyclerView;
import com.example.wassim.sprinkle.widgets.SimpleTouchCallback;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    /*
     * Différents élements de la vue principale
     */
    private GardenRecyclerView mRecyclerView;
    private AdapterPlants mPlantsAdapter;
    private View mEmptyPlants;
    private Toolbar mToolbar;

    // La DAO permettant les différents acces a la bdd
    private PlantDao plantDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add = (Button) findViewById(R.id.btn_add);
        add.setOnClickListener(this);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mEmptyPlants = findViewById(R.id.empty_plants);

        setSupportActionBar(mToolbar);

        // Initialisation de la DAO pour la gestion de la BDD
        this.plantDao = new PlantFactory(getApplication()).getPlantDao();

        // Initialisation du RecyclerView
        mRecyclerView = (GardenRecyclerView) findViewById(R.id.plant_list);
        // On cache la toolbar si il n y pas de plantes
        mRecyclerView.hideIfNoPlants(mToolbar);
        // On affiche la vue représentant le splash screen quand il n y a aucune plante
        mRecyclerView.showIfPlants(mEmptyPlants);
        mPlantsAdapter = new AdapterPlants(this, this.plantDao.loadAll());
        mRecyclerView.setAdapter(mPlantsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Implementation  d'un callback permettant la gestion du swipe pour la suppression d'un element du recyclerview
        SimpleTouchCallback callback = new SimpleTouchCallback(this.mPlantsAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(this.mRecyclerView);

        //Caching du background afin que l'application ne plante pas ( eviter un outofmemory)
        initBackgroundImage();
    }

    /**
     * Caching de l'image de fond pour éviter les memory leak
     */
    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.app_bg);
        Glide.with(this)
                .load(R.drawable.bg)
                .centerCrop()
                .into(background);
    }

    /**
     * Quand on reviens sur l'application, on mets a jours létat des plantes
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.mPlantsAdapter.updateDataSet();
    }

    /**
     * Initialisation du menu de la toolbar
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Gestion du cliques sur les menus présents sur la toolbar
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_action:
                // On demarra l'activity d'ajout en attendant un résultat
                Intent intent = new Intent(MainActivity.this, AddPlant.class);
                startActivityForResult(intent, 202);
            break;

            case R.id.delete_action:
                // On vide la base de données
                this.plantDao.deleteAll();
                // ON vide la liste que l'adapter maintiens
                this.mPlantsAdapter.clearItems();
                // On supprime l'ensemble des fichiers photos de l'application
                String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Sprinkle"));
                if ( FileUtil.exists(path) )
                    FileUtil.deleteDirectory(path);
            break;

            case R.id.date_picker:
                DialogFragment fragment = new DatePicker();
                fragment.show(getSupportFragmentManager(), "datepicker");
            break;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * Methode permettant la gestion du clique sur le boutton ajouter une plante quand la BDD est vide
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_add:
                generateFixtures();
            break;
        }
    }

    /**
     * Fonction permettant de créer 10 plantes examples pour pouvoir utiliser directement l'application
     * Utile à des fins de tests et de debug
     */
    private void generateFixtures()
    {
        String plantNames[] = {"Abricotier", "Acacia", "Agapanthe", "Cyprès", "Dahlia", "Datura", "Narcisse", "Oeillet", "Olivier", "Oranger"};
        int wateringFrequency[] = {2, 3, 6, 4, 2, 7, 2, 3, 2, 5};

        for (int i = 0; i < 10; i++) {
            Plant plant = new Plant(null, plantNames[i], wateringFrequency[i]);
            this.mPlantsAdapter.addToPlants(plant);
            this.plantDao.insert(plant);
        }
    }

    /**
     * Methode permettant de gérer les resultats retournés par un intent
     * On verifie que le request code est bien le bon, et que le resul code est de type RESULT_OK
     * On fait les traitements par la suite
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 202)
        {
            switch (resultCode)
            {
                case RESULT_OK:
                    int wateringFrequency = data.getIntExtra(IntentKeys.PLANT_WATERING_FQ, 0);
                    String plantName = data.getStringExtra(IntentKeys.PLANT_NAME);
                    String picturePath = (data.getStringExtra(IntentKeys.PLANT_PICTURE_PATH) == null) ? "" : data.getStringExtra(IntentKeys.PLANT_PICTURE_PATH);

                    if(wateringFrequency != 0 && !plantName.isEmpty() && picturePath.isEmpty())
                    {
                        Plant plant = new Plant(null, plantName, wateringFrequency);
                        plantDao.insert(plant);
                        this.mPlantsAdapter.addToPlants(plant);
                    }
                    else if (wateringFrequency != 0 && !plantName.isEmpty() && !picturePath.isEmpty())
                    {
                        Plant plant = new Plant(null, plantName, wateringFrequency, picturePath);
                        plantDao.insert(plant);
                        this.mPlantsAdapter.addToPlants(plant);
                    }

                break;

                case RESULT_CANCELED:
                break;
            }
        }
    }

    /**
     * Methode permttant de récupérer les nouvelles inforamtions de date
     * lorsqu'on modifie cette dernière à partir du bouton de la toolbar
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth)
    {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        // On envoie la nouvelle date à l'adapter pour qu'il mette à jours sa liste
        this.mPlantsAdapter.setDateToCompareTo(c.getTimeInMillis());
    }
}
