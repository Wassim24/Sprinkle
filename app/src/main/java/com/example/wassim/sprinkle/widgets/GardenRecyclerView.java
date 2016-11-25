package com.example.wassim.sprinkle.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.example.wassim.sprinkle.extras.ViewsUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * RecyclerView custom permettant d'afficher la liste des elements
 * si elle est remplie, sinon un autre layout si elle est vide
 */
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

    /**
     * Methode permettant de setter l'adapter au recyclerview
     * @param adapter
     */
    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if( adapter != null )
            adapter.registerAdapterDataObserver(this.mObserver);

        this.mObserver.onChanged();
    }

    /**
     * Methode permettant de cacher des views
     * @param views
     */
    public void hideIfNoPlants(View...views)
    {
        mNonEmptyViews = Arrays.asList(views);
    }

    /**
     * Methode permettant d'afficher des view
     * @param views
     */
    public void showIfPlants(View...views)
    {
        mEmptyViews = Arrays.asList(views);
    }


    /**
     * Gestion des différents états du recyclerview
     */
    private void toggleViews()
    {
        if(getAdapter()!=null && !mEmptyViews.isEmpty() && !mNonEmptyViews.isEmpty())
        {
            // Si l'adapter ne contient aucun élément
            if(getAdapter().getItemCount()==0)
            {
                // Affiche les views qui sont censées s'afficher quand c'est vide
                ViewsUtil.showViews(mEmptyViews);

                // Enleve le recyclerview
                this.setVisibility(View.GONE);

                // Affiche les views qui sont censées ne pas s'afficher quand c'est rempli
                ViewsUtil.hideViews(mNonEmptyViews);
            }
            else
            {
                // Affiche les views qui sont censées s'afficher quand c'est vide
                ViewsUtil.showViews(mNonEmptyViews);

                // permet d'afficher le recyclerview
                this.setVisibility(View.VISIBLE);

                // Affiche les views qui sont censées ne pas s'afficher quand c'est rempli
                ViewsUtil.hideViews(mEmptyViews);
            }
        }
    }

}
