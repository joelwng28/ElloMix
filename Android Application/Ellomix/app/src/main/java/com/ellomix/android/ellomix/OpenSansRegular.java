package com.ellomix.android.ellomix;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Abhi on 12/17/2016.
 */

public class OpenSansRegular extends TextView {

    public OpenSansRegular(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public OpenSansRegular(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public OpenSansRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("OpenSans-Regular.ttf", context);
        setTypeface(customFont);
    }
}
