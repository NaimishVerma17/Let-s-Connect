package com.example.itakg.twitterclone.Extras;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by itakg on 10/25/2017.
 */

public class MyProgressDialog {
    public static ProgressDialog myDialog(Context context,String message)
    {
        ProgressDialog dialog=new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;

    }
}
