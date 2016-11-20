package com.example.wassim.sprinkle.adapters;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wassim.sprinkle.R;
import com.example.wassim.sprinkle.app.PlantDetails;
import com.example.wassim.sprinkle.beans.Plant;
import com.example.wassim.sprinkle.beans.PlantDao;
import com.example.wassim.sprinkle.extras.DateHelper;
import com.example.wassim.sprinkle.extras.IntentKeys;
import com.example.wassim.sprinkle.factory.PlantFactory;
import com.example.wassim.sprinkle.widgets.StatusChip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wassim on 11/19/16.
 */

public class AdapterPlants extends RecyclerView.Adapter<AdapterPlants.PlantHolder>
{
    private LayoutInflater mInflater;

    public void setmItem(List<Plant> mItem) {
        this.mItem = mItem;
    }

    private List<Plant> mItem = new ArrayList<>();

    public AdapterPlants(Context context, List<Plant> plants) {
        this.mInflater = LayoutInflater.from(context);
        this.mItem = plants;
    }

    @Override
    public PlantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View plant_row = this.mInflater.inflate(R.layout.plant_row, parent, false);
        PlantHolder holder = new PlantHolder(plant_row);
        return holder;
    }

    @Override
    public void onBindViewHolder(PlantHolder holder, int position)
    {
        Plant plant = this.mItem.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - H:mm");
        long nextWateringDate = DateHelper.addDaysToDate(plant.getLastWateringAt(), plant.getWateringFrequency());

        if(position % 2 == 0)
        {
                holder.mStatus.setBackground(new StatusChip());
        }
        else
        {
                holder.mStatus.setBackground(new StatusChip(0xFFFFFFFF));
        }
        holder.mPlantName.setText(plant.getName());
        holder.mNextWatering.setText(sdf.format(nextWateringDate));
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

    static class PlantHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        PlantDao plantDao;

        TextView mPlantName;
        TextView mNextWatering;
        TextView mStatus;

        public PlantHolder(View itemView) {
            super(itemView);
            mPlantName = (TextView) itemView.findViewById(R.id.plant_name);
            mNextWatering = (TextView) itemView.findViewById(R.id.watering_date);
            mStatus = (TextView) itemView.findViewById(R.id.status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

            plantDao = PlantFactory.getInstance().getPlantDao();
            Plant plant = plantDao.loadByRowId(getPosition());


            if(plant != null)
            {
                Intent plantDetails = new Intent(v.getContext(), PlantDetails.class);
                plantDetails.putExtra(IntentKeys.PLANT_NAME, plant.getName());
                plantDetails.putExtra(IntentKeys.PLANT_DATE, plant.getCreatedAt());
                plantDetails.putExtra(IntentKeys.PLANT_PREVIOUS_WATERING, plant.getLastWateringAt());
                plantDetails.putExtra(IntentKeys.PLANT_PICTURE_PATH, plant.getPicturePath());
                v.getContext().startActivity(plantDetails);
            }
        }
    }
}
