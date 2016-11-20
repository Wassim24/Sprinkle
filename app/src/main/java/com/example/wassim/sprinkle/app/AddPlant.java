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


public class AddPlant extends AppCompatActivity implements View.OnClickListener{

    private NumberPicker mNumberPicker;
    private Toolbar mToolBar;
    private FloatingActionButton mTakePicture;
    private TextView mPlantName;

    private File plantPicture;
    private String picturePath = Environment.DIRECTORY_PICTURES + "/Sprinkle";

    private int mPictureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.mToolBar);

        this.mNumberPicker = (NumberPicker) findViewById(R.id.input_plant_fq);
        this.mNumberPicker.setMinValue(1);
        this.mNumberPicker.setMaxValue(365);

        this.mTakePicture = (FloatingActionButton) findViewById(R.id.take_picture);
        this.mTakePicture.setOnClickListener(this);

        this.mPlantName = (TextView) findViewById(R.id.input_plant_name);

        this.mPictureId = -1;

        initBackground();
    }

    private void prepareIntentForSendingData()
    {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.validate_action:

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(IntentKeys.PLANT_WATERING, this.mNumberPicker.getValue());
                intent.putExtra(IntentKeys.PLANT_NAME, this.mPlantName.getText().toString());
                if(this.mPictureId != -1)
                    intent.putExtra(IntentKeys.PLANT_PICTURE_PATH, this.mPictureId);

                setResult(Activity.RESULT_OK, intent);
                finish();

            break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void initBackground()
    {
        ImageView background = (ImageView) findViewById(R.id.add_bg);
        Glide.with(this)
                .load(R.drawable.lnd)
                .centerCrop()
                .into(background);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.take_picture:
                takePictureAndProcess();
            break;
        }

    }

    private void takePictureAndProcess()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.mPictureId = (int) PlantFactory.getInstance().getPlantDao().count() + 1;
        this.plantPicture = new File(Environment.getExternalStoragePublicDirectory(this.picturePath), "plant_"+this.mPictureId+".jpg");

        Uri tempURI = Uri.fromFile(this.plantPicture);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempURI);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 0)
        {
            switch (resultCode)
            {
                case Activity.RESULT_OK:

                    if(this.plantPicture.exists())
                        Toast.makeText(this, R.string.took_picture_success, Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(this, R.string.took_picture_failure, Toast.LENGTH_LONG).show();
                break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, R.string.took_picture_failure, Toast.LENGTH_LONG).show();
                break;
            }
        }

    }
}

