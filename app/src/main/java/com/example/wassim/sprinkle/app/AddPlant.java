package com.example.wassim.sprinkle.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wassim.sprinkle.R;
import com.example.wassim.sprinkle.extras.IntentKeys;
import com.example.wassim.sprinkle.factory.PlantFactory;

import java.io.File;
import java.util.Random;


public class AddPlant extends AppCompatActivity implements View.OnClickListener{

    // Répertoire racine des photos
    private final String mPictureRoot = Environment.DIRECTORY_PICTURES + "/Sprinkle";

    // Differents elements de la vue d'ajout/edition
    private NumberPicker mNumberPicker;
    private TextView mPlantName;
    private Toolbar mToolBar;

    // variables utilitaires pour la gestion des photos
    private File mPlantPicture;
    private String mTempEditPath;
    private String mPictureName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Ajouter une plante");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.mNumberPicker = (NumberPicker) findViewById(R.id.input_plant_fq);
        this.mNumberPicker.setMinValue(2);
        this.mNumberPicker.setMaxValue(365);

        FloatingActionButton mTakePicture = (FloatingActionButton) findViewById(R.id.take_picture);
        mTakePicture.setOnClickListener(this);

        this.mPlantName = (TextView) findViewById(R.id.input_plant_name);

        this.mPictureName = "";
        this.mTempEditPath = "";

        // appel de la fonction getEditData seulement dans le cas de l'édition
        getEditData();
        // caching du background
        initBackground();
    }

    /**
     * Méthode permettant d'initialiser les différents champs
     * si on est en mode edition
     */
    private void getEditData()
    {

        Intent edit_caller = getIntent();

        if(edit_caller.getLongExtra(IntentKeys.PLANT_ID, -1) != -1)
        {
            this.mNumberPicker.setValue(edit_caller.getIntExtra(IntentKeys.PLANT_WATERING_FQ, 2));
            this.mPlantName.setText(edit_caller.getStringExtra(IntentKeys.PLANT_NAME));
            this.mTempEditPath = edit_caller.getStringExtra(IntentKeys.PLANT_PICTURE_PATH);
            getSupportActionBar().setTitle("Éditer une plante");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Initialisation des menus de la toolbar
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_add_action, menu);
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
        int id = item.getItemId();
        switch (id)
        {
            case R.id.validate_action:

                Intent intent = null;
                long plantId = getIntent().getLongExtra(IntentKeys.PLANT_ID,-1);

                if(plantId == -1)
                    intent = new Intent(this, MainActivity.class);
                else
                {
                    intent = new Intent(this, PlantDetails.class);
                    intent.putExtra(IntentKeys.PLANT_ID, plantId);
                }

                intent.putExtra(IntentKeys.PLANT_WATERING_FQ, this.mNumberPicker.getValue());
                intent.putExtra(IntentKeys.PLANT_NAME, this.mPlantName.getText().toString());
                if(!this.mPictureName.isEmpty()) {
                    intent.putExtra(IntentKeys.PLANT_PICTURE_PATH, this.mPictureName);
                }

                setResult(Activity.RESULT_OK, intent);
                finish();

            break;

            case android.R.id.home:
                onBackPressed();
            break;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * Méthode permettant de faire le caching de l'image de fond
     */
    private void initBackground()
    {
        ImageView background = (ImageView) findViewById(R.id.add_bg);
        Glide.with(this)
                .load(R.drawable.bg)
                .centerCrop()
                .into(background);
    }

    /**
     * Gestion du clique sur le floatingactionbutton pour la prise de photo
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.take_picture:
                // Execution de la methode qui permet de capturer l'image et la stocker
                takePictureAndProcess();
            break;
        }

    }

    /**
     * Methode permettant de prendre la photo et de l'enregistrer
     */
    private void takePictureAndProcess()
    {
        // On verifie si mTempEditPath est vide au quel cas la plante ne possède pas de photo sinon on supprime l'acienne photo car on va en prendre une nouvelle
        if(!this.mTempEditPath.isEmpty())
        {
            // On cree un fichier contenant la photo et on le supprime
            this.mPlantPicture = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(this.mTempEditPath)));

            // Suppression de l'ancienne photo
            if(this.mPlantPicture.exists()) {
                this.mPlantPicture.delete();
            }
        }

        // On verifie si le dossier des photo existe ou pas, sinon on le cree
        this.mPlantPicture = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(this.mPictureRoot)));
        if(!this.mPlantPicture.exists())
            this.mPlantPicture.mkdir();

        // On lance l'intent de la camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Random rnd = new Random();
        // On génère un nom aléatoire pour la nouvelle photo
        this.mPictureName = "plant_" + PlantFactory.getInstance().getPlantDao().count() + "_" + rnd.nextInt(1000) +".jpg";
        this.mPlantPicture = new File(Environment.getExternalStoragePublicDirectory(this.mPictureRoot), this.mPictureName);

        // On paramètre le dossier d'output et la qualité de l'image
        Uri tempURI = Uri.fromFile(this.mPlantPicture);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempURI);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        // On lance l'intent
        startActivityForResult(intent, 0);
    }

    /**
     * Méthode s'executant après la prise de photo pour afficher un toast de confirmation ou d'infirmation de prise de phto
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 0)
        {
            switch (resultCode)
            {
                case Activity.RESULT_OK:
                    if(this.mPlantPicture.exists())
                        Toast.makeText(this, "La photo a été prise", Toast.LENGTH_LONG).show();
                break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, R.string.took_picture_failure, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    /**
     * Gestion du clique sur le bouton retour
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

