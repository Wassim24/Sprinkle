package com.example.wassim.sprinkle.extras;

import android.view.View;

import java.util.List;

/**
 * Created by wassim on 11/19/16.
 */

public class Util {

    public static void showViews(List<View> views)
    {
        for (View view : views)
            view.setVisibility(View.VISIBLE);
    }

    public static void hideViews(List<View> views)
    {
        for (View view : views)
            view.setVisibility(View.GONE);
    }
}
