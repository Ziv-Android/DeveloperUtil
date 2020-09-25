package com.ziv.developer;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ziv.developer.utils.LogUtil;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test();

        LogUtil.d(TAG, "msg content");
    }

    private void test(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            Log.d(TAG, "test: stackTrace[" + i + "]: " + stackTrace[i].toString());
        }
    }
}