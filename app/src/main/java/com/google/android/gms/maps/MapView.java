package com.google.android.gms.maps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class MapView extends View {
    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
