package com.example.wassim.sprinkle.widgets;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/**
 * Classe définissant un shapedrawable custom pour caractériser l'état d'arrosage
 */
public class StatusChip extends ShapeDrawable {

    /**
     * Constructeur prenant en paramètre la couleur à affecter au cercle
     * @param color
     */
    public StatusChip(int color) {
        this.setShape(new OvalShape());
        this.getPaint().setColor(color);
    }
}
