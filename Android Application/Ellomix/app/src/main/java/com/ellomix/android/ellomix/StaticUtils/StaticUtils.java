package com.ellomix.android.ellomix.StaticUtils;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by jhurt on 2/19/17.
 */

public class StaticUtils {
    public static RequestBody argsToJSON(Map<String, Object> args) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(args)).toString());
    }
}
