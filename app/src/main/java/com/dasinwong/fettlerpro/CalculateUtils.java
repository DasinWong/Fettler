package com.dasinwong.fettlerpro;

import android.content.Context;
import android.widget.Toast;

public class CalculateUtils {

    public void calculate(Context context) {
        int a = 666;
        int b = 0;
        Toast.makeText(context, "result is " + a / b, Toast.LENGTH_SHORT).show();
    }
}
