package com.example.wassim.sprinkle.adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wassim.sprinkle.R;
import com.example.wassim.sprinkle.beans.Plant;

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

    public AdapterPlants(Context context)
    {
        this.mInflater = LayoutInflater.from(context);
    }

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
    public void onBindViewHolder(PlantHolder holder, int position) {
        holder.mPlantName.setText("lol");
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

    public static class PlantHolder extends RecyclerView.ViewHolder
    {
        TextView mPlantName;

        public PlantHolder(View itemView) {
            super(itemView);
            mPlantName = (TextView) itemView.findViewById(R.id.plant_name);
        }
    }



}
