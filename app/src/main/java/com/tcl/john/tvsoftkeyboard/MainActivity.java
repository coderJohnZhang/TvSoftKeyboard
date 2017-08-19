package com.tcl.john.tvsoftkeyboard;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tcl.john.globalsoftkeyboard.GlobalSoftKeyboard;
import com.tcl.john.tvsoftkeyboard.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setBind(this);
    }

    public void rename(View view) {
        GlobalSoftKeyboard globalSoftKeyboard = new GlobalSoftKeyboard(this, "ChannelName");

        globalSoftKeyboard.setKeyboardListenner(new GlobalSoftKeyboard.KeyboardListener()
        {
            public void getText(String text)
            {
                Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
            }
        });
        globalSoftKeyboard.show();
    }

}
