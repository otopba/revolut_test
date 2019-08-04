package com.otopba.revolut.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;

import androidx.annotation.NonNull;

public class ThemeUtils {

    @NonNull
    public static Drawable recyclerItemBackground(int rippleColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(rippleColor), null, getRippleMask());
        } else {
            return stateListDrawable(rippleColor);
        }
    }

    @NonNull
    private static Drawable getRippleMask() {
        RectShape r = new RectShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(r);
        shapeDrawable.getPaint().setColor(Color.RED);
        return shapeDrawable;
    }

    @NonNull
    private static StateListDrawable stateListDrawable(int pressedColor) {
        Drawable pressedDrawable = new ColorDrawable(pressedColor);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        states.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
        states.addState(new int[]{android.R.attr.state_activated}, pressedDrawable);
        states.addState(new int[]{}, null);
        return states;
    }

}
