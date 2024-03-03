package com.example.login2.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.login2.R;

public class CustomProgressDialog  extends Dialog{
    public CustomProgressDialog(@NonNull Context context) {
        super(context);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.gravity = Gravity.CENTER_HORIZONTAL;
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        getWindow().setAttributes(params);

        View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog,null,false);
        setContentView(view);
    }
}

