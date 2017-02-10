package com.ellomix.android.ellomix.Model;

import android.content.Context;
import android.widget.MediaController;

/**
 * Created by ATH-AJT2437 on 2/8/2017.
 */

public class MusicController extends MediaController {

    public MusicController(Context context) {
        super(context);
    }

    public MusicController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

}
