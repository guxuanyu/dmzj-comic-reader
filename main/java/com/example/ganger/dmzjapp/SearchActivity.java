package com.example.ganger.dmzjapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    private AppCompatEditText searchEditText;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar= (Toolbar) findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchEditText= (AppCompatEditText) findViewById(R.id.search_edittext);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    String s=v.getText().toString();
                    String url= String.format("http://m.dmzj.com/search/%s.html",s);
                    Intent intent=new Intent(SearchActivity.this,WebActivity.class);
                    intent.putExtra("url",url);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}
