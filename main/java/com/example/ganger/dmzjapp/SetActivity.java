package com.example.ganger.dmzjapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Toast;

public class SetActivity extends AppCompatActivity {

    private SwitchCompat switchCompat;
    private AppCompatEditText appCompatEditText;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        toolbar= (Toolbar) findViewById(R.id.view);
        toolbar.setTitle("开发者界面");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        switchCompat= (SwitchCompat) findViewById(R.id.is_show_img_switch);
        appCompatEditText= (AppCompatEditText) findViewById(R.id.edittext);
        SharedPreferences sharedPreferences=getSharedPreferences("setting",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        boolean isshow=sharedPreferences.getBoolean("isshowimage",false);
        switchCompat.setChecked(isshow);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("isshowimage",isChecked);
                    //Toast.makeText(SetActivity.this, String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                    editor.commit();
                    switchCompat.setChecked(isChecked);
                }
                else {
                    editor.putBoolean("isshowimage",isChecked);
                    //Toast.makeText(SetActivity.this, String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                    editor.commit();
                    switchCompat.setChecked(isChecked);
                }
            }
        });
        String lastUrl=sharedPreferences.getString("lasturl","default");
        appCompatEditText.setText(lastUrl);
    }
}
