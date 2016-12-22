package com.ellomix.android.ellomix.Style;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Abhi on 12/17/2016.
 * Code for Font Cache taken from https://futurestud.io/tutorials/custom-fonts-on-android-extending-textview
 */

public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }
}
