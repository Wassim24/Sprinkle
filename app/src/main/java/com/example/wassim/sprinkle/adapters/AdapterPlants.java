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
import com.example.wassim.sprinkle.extras.DateUtil;
import com.example.wassim.sprinkle.extras.IntentKeys;
import com.example.wassim.sprinkle.factory.PlantFactory;
import com.example.wassim.sprinkle.widgets.StatusChip;
import com.example.wassim.sprinkle.widgets.SwipeListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterPlants extends RecyclerView.Adapter<AdapterPlants.PlantHolder> implements SwipeListener
{
    // Un layout inflater pour injecter la structure de chaque element du recyclerview
    private LayoutInflater mInflater;
    // La liste des plantes
    private List<Plant> mItem = new ArrayList<>();

    // La date avec la quelle on compare pour changer la couleur des pastilles sur le recyclerview.
    private long dateToCompareTo;

    /**
     * Constructeur du PlantAdapter, permettant d'envoyer le contexte d'execution, et la liste des plantes
     * @param context
     * @param plants
     */
    public AdapterPlants(Context context, List<Plant> plants) {
        this.mInflater = LayoutInflater.from(context);
        this.mItem = plants;
        this.dateToCompareTo = System.currentTimeMillis();
    }

    /**
     * On injecte la structure de l'élément dans sont parent @param parent
     * et on retourne un viewholder contenant l'objet créé
     * @param parent
     * @param viewType
     * @return PlantHolder
     */
    @Override
    public PlantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View plant_row = this.mInflater.inflate(R.layout.plant_row, parent, false);
        return new PlantHolder(plant_row);
    }

    /**
     * Initialisation des différents paramètres de la structure de chaque element
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(PlantHolder holder, int position)
    {
        Plant plant = this.mItem.get(position);

        // On calcule la date du prochain arrosage
        long nextWateringDate = DateUtil.addDaysToDate(plant.getLastWateringAt(), plant.getWateringFrequency());
        // On compare les dates d'aujourdhui et celle du prochain arrosage pour déduire la couleur de la pastille
        int chipColor = DateUtil.compareTwoDate(nextWateringDate, this.dateToCompareTo);

        // 2 == c'est arrosé, 0 c'est la veille, sinon la date est dépassée
        if(chipColor == 1)
            holder.mStatus.setBackground(new StatusChip(0xFF4D8E46));
        else
            if (chipColor == 0)
                holder.mStatus.setBackground(new StatusChip(0xFFF9A825));
        else
            holder.mStatus.setBackground(new StatusChip(0xFFE53131));

        // On met le nom et la date du prochain arrosage dans le recyclerview
        holder.mPlantName.setText(plant.getName());
        holder.mNextWatering.setText(DateUtil.formatDate(nextWateringDate));
    }

    /**
     * Retourne le nombre d'éléments contenus dans le recyclerview
     * @return
     */
    @Override
    public int getItemCount() {
        return mItem.size();
    }

    /**
     * Gestion de la suppression de l'élement de la BDD, et mise à jour de l'adapter
     * @param position
     */
    @Override
    public void onSwipe(int position)
    {
        if(!this.mItem.isEmpty())
        {
            PlantFactory.getInstance().getPlantDao().delete(this.mItem.get(position));
            this.mItem.remove(position);
            this.notifyItemRemoved(position);
        }
    }

    /**
     * Ajouter un élément à l'adapter et mise à jour de ce dernier
     * @param plant
     */
    public void addToPlants(Plant plant)
    {
        this.mItem.add(plant);
        this.notifyItemInserted(this.mItem.size());
    }

    /**
     * On met a jour la date pour a des fins de tests
     * @param dateToCompareTo
     */
    public void setDateToCompareTo(long dateToCompareTo) {
        this.dateToCompareTo = dateToCompareTo;
        this.updateDataSet();
    }

    /**
     * Nettoyage de la liste des éléments de l'adapter
     */
    public void clearItems()
    {
        int total = this.mItem.size();
        this.mItem.clear();
        this.notifyItemRangeChanged(0, total);
    }

    /**
     * Mise a jour de l'adapter
     */
    public void updateDataSet()
    {
        this.notifyDataSetChanged();
    }


    /**
     * Le holder pour chaque element du recyclerview
     */
    static class PlantHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        // DAO permettant les différents accés a la BDD
        private PlantDao plantDao;

        // Les différents composants de la structure d'un élément
        private TextView mPlantName;
        private TextView mNextWatering;
        private TextView mStatus;

        /**
         * Constructeur du PlantHolder
         * @param itemView
         */
        public PlantHolder(View itemView) {
            super(itemView);
            mPlantName = (TextView) itemView.findViewById(R.id.plant_name);
            mNextWatering = (TextView) itemView.findViewById(R.id.watering_date);
            mStatus = (TextView) itemView.findViewById(R.id.status);
            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        /**
         * Gestion du clique sur un element pour consulter les détails d'une plante
         * @param v
         */
        @Override
        public void onClick(View v)
        {
            // Selon la position de l'élément on récupère la plante correspondante grace à la DAO
            Plant plant = getPlant();

            // Si on a un element on envoie les informations via un intent vers l'activity des details
            if(plant != null)
            {
                Intent plantDetails = new Intent(v.getContext(), PlantDetails.class);
                plantDetails.putExtra(IntentKeys.PLANT_NAME, plant.getName());
                plantDetails.putExtra(IntentKeys.PLANT_DATE, plant.getCreatedAt());
                plantDetails.putExtra(IntentKeys.PLANT_PREVIOUS_WATERING, plant.getLastWateringAt());
                plantDetails.putExtra(IntentKeys.PLANT_NEXT_WATERING, DateUtil.addDaysToDate(plant.getLastWateringAt(), plant.getWateringFrequency()));
                plantDetails.putExtra(IntentKeys.PLANT_PICTURE_PATH, plant.getPicturePath());
                plantDetails.putExtra(IntentKeys.PLANT_WATERING_FQ, plant.getWateringFrequency());
                plantDetails.putExtra(IntentKeys.PLANT_ID, plant.getId());
                v.getContext().startActivity(plantDetails);

                plantDao = null;
                plant = null;
            }
        }

        /**
         * Gestion du long clique pour mettre à jour l'arrosage
         * @param v
         * @return boolean
         */
        @Override
        public boolean onLongClick(View v)
        {
            Plant plant = getPlant();

            if(plant != null)
            {
                plant.setLastWateringAt(System.currentTimeMillis());
                plantDao.update(plant);
                this.mStatus.setBackground(new StatusChip(0xFF4D8E46));

                plantDao = null;
                plant = null;
            }

            return true;
        }


        /**
         * Permet d'avoir une instance Plant à partir de l'element sur le quel on cliqué
         * @return
         */
        private Plant getPlant() {
            plantDao = PlantFactory.getInstance().getPlantDao();
            List<Plant> plants_list = plantDao.loadAll();
            long key = plantDao.getKey(plants_list.get(getAdapterPosition()));
            return plantDao.loadByRowId(key);
        }
    }
}
