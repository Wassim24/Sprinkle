package com.example.wassim.sprinkle.extras;

import android.view.View;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Classe utilitaire permettant des opérations sur les vues
 */
public class ViewsUtil {

    /**
     * Méthode permettant d'afficher une liste de views
     * @param views
     */
    public static void showViews(List<View> views)
    {
        for (View view : views)
            view.setVisibility(View.VISIBLE);
    }

    /**
     * Méthode permettant de masquer une liste de views
     * @param views
     */
    public static void hideViews(List<View> views)
    {
        for (View view : views)
            view.setVisibility(View.GONE);
    }
}
