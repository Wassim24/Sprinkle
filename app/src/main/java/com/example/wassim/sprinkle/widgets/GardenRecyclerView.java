package com.example.wassim.sprinkle.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.example.wassim.sprinkle.extras.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GardenRecyclerView extends RecyclerView
{
    // Pour éviter les null exceptions
    private List<View> mNonEmptyViews = Collections.emptyList();
    private List<View> mEmptyViews = Collections.emptyList();

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }
    };

    public GardenRecyclerView(Context context) {
        super(context);
    }

    public GardenRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GardenRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if( adapter != null )
        {
            adapter.registerAdapterDataObserver(this.mObserver);
        }

        this.mObserver.onChanged();
    }

    public void hideIfNoPlants(View...views)
    {
        mNonEmptyViews = Arrays.asList(views);
    }

    public void showIfPlants(View...views)
    {
        mEmptyViews = Arrays.asList(views);
    }


    private void toggleViews()
    {
        if(getAdapter()!=null && !mEmptyViews.isEmpty() && !mNonEmptyViews.isEmpty())
        {
            if(getAdapter().getItemCount()==0)
            {
                // Affiche toutes les vues vides
                Util.showViews(mEmptyViews);

                // Enleve le recyclerview
                this.setVisibility(View.GONE);

                // Permet de "supprimer" toutes les vues qui doivent l'être
                Util.hideViews(mNonEmptyViews);
            }
            else
            {
                // Affiche toutes les vues non vides
                Util.showViews(mNonEmptyViews);

                // permet d'afficher le recyclerview
                this.setVisibility(View.VISIBLE);

                // Permet de "supprimer" toutes les vues qui doivent l'être
                Util.hideViews(mEmptyViews);
            }
        }
    }

}
