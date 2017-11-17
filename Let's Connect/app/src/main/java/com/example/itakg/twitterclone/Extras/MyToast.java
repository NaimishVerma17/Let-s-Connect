package com.example.itakg.twitterclone.Extras;

import android.content.Context;
import android.widget.Toast;

import com.example.itakg.twitterclone.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

/**
 * Created by itakg on 10/24/2017.
 */

public class MyToast {
    public static void make(Context context,String message)
    {
        StyleableToast.makeText(context, message, Toast.LENGTH_LONG, R.style.MyToast).show();
    }
}
