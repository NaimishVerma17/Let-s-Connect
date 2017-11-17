package com.example.itakg.twitterclone.Extras;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by itakg on 10/24/2017.
 */

public class MyAlertDialogs {
    public static void myAlert(Context context, String Title, String Message) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert
                .setTitle(Title)
                .setMessage(Message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

    }
}
