package com.example.wassim.sprinkle.widgets;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Callback pour la gestion des swipes sur le recyclerview
 */
public class SimpleTouchCallback extends ItemTouchHelper.Callback {

    private SwipeListener mSwiperListener;

    // Constructeur permettant d'initiliser le listener du swipe
    public SimpleTouchCallback(SwipeListener swipeListener)
    {
        this.mSwiperListener = swipeListener;
    }

    // Specification de la direction du swipe
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    // On renvoie l'information de la position à l'adapter pour supprimee l'element
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        this.mSwiperListener.onSwipe(viewHolder.getAdapterPosition());
    }
}
