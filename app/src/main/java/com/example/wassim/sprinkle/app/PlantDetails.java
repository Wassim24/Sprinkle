package com.example.wassim.sprinkle.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wassim.sprinkle.R;
import com.example.wassim.sprinkle.extras.IntentKeys;

import java.io.File;
import java.net.URI;

public class PlantDetails extends AppCompatActivity {

    TextView plantName;
    TextView plantDate;;
    TextView previousWater;
    TextView nextWater;

    String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initPlantDetails();
        initBackgroundImage();
    }

    private void initPlantDetails() {
        plantName = (TextView) findViewById(R.id.detail_plant_name);
        plantDate = (TextView) findViewById(R.id.detail_plant_date);
        previousWater = (TextView) findViewById(R.id.detail_previous_water);
        nextWater = (TextView) findViewById(R.id.detail_next_water);

        Intent details = getIntent();
        plantName.setText(details.getStringExtra(IntentKeys.PLANT_NAME));
        this.picturePath = Environment.DIRECTORY_PICTURES +"/Sprinkle/" + details.getStringExtra(IntentKeys.PLANT_PICTURE_PATH);

    }

    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.plant_photo);
        Glide.with(this)
                .load(new File(Environment.getExternalStoragePublicDirectory(this.picturePath)+".jpg"))
                .centerCrop()
                .into(background);
    }
}
