package com.eric.android.view.excelpanel.util;

import android.content.Context;

public class DPUtil {
    public static int dp2px(Context context, float dp) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }
}
