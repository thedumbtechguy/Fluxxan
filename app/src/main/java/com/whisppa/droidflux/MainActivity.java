package com.whisppa.droidflux;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whisppa.droidfluxlib.StoreListener;


public class MainActivity extends ActionBarActivity implements StoreListener {

    private Button btn;
    private TextView txt;
    private Button asyncBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);
        asyncBtn = (Button) findViewById(R.id.asyncBtn);
        txt = (TextView) findViewById(R.id.txt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp.getFlux().getActions().getUser();
            }
        });

        asyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp.getFlux().getActions().getUserAsync();
            }
        });
    }


    protected void onResume() {
        super.onResume();

        MyStore store = (MyStore) MyApp.getFlux().getStore(MyStore.class.getName());
        store.addListener(this);
        txt.setText(store.getState());
    }


    protected void onStop() {
        super.onStop();

        MyStore store = (MyStore) MyApp.getFlux().getStore(MyStore.class.getName());
        store.removeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChanged() {
        MyStore store = (MyStore) MyApp.getFlux().getStore(MyStore.class.getName());
        txt.setText(store.getState());
    }
}
