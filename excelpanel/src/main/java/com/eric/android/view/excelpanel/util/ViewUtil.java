package com.eric.android.view.excelpanel.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * TextView工具
 */
public class ViewUtil {
    public static void copyText(Context context, TextView textView) {
        if (context == null || textView == null || TextUtils.isEmpty(textView.getText())) return;
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        String str = textView.getText().toString();
        if (cm != null && !TextUtils.isDigitsOnly(str)) {
            cm.setText(str);
        }
    }
}
