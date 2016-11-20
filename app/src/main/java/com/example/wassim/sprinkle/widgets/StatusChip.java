package com.example.wassim.sprinkle.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

/**
 * Created by wassim on 11/20/16.
 */

public class StatusChip extends ShapeDrawable {

    public StatusChip() {
        this.setShape(new OvalShape());
        this.getPaint().setColor(0xFFFF00FF);
    }

    public StatusChip(int i) {
        this.setShape(new OvalShape());
        this.getPaint().setColor(i);
    }
}
