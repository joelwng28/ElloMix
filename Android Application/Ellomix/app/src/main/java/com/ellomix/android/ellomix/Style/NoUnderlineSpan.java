package com.ellomix.android.ellomix.Style;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;

import com.ellomix.android.ellomix.R;

/**
 * Created by Akshay on 3/9/17.
 */

public class NoUnderlineSpan extends UnderlineSpan {
    private static Parcelable.Creator CREATOR;

    public NoUnderlineSpan() {}

    public NoUnderlineSpan(Parcel src) {}

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }
}
