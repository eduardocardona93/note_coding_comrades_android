package com.madt.note_coding_comrades_android.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class NoteUtils {
    public static void HideSoftKeyboard(Context mContext) {
        try {
            if (mContext != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                if (((Activity) mContext).getCurrentFocus() != null)
                    inputMethodManager.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showProgressBar(Activity activity) {
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLog( String message_about, String message) {
        Log.e("Log", message_about + "::::" + message);
    }

    public static void showSnackBar(Activity activity, String message) {

        final Snackbar snackbar = Snackbar.make((activity).findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setDuration(30000);
        snackbar.setTextColor(Color.RED);
        snackbar.setActionTextColor(Color.BLACK);
        snackbar.setBackgroundTint(Color.LTGRAY);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
