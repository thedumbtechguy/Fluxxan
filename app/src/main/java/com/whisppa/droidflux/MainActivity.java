package com.whisppa.droidflux;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.ui.StoreListenerActivity;
import com.whisppa.droidfluxlib.utils.ThreadUtils;


public class MainActivity extends StoreListenerActivity {

    private Button btn;
    private TextView txt;
    private Button asyncBtn;

    private Button btn2;
    private Button asyncBtn2;
    private TextView txt2;


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


        btn2 = (Button) findViewById(R.id.btn2);
        asyncBtn2 = (Button) findViewById(R.id.asyncBtn2);
        txt2 = (TextView) findViewById(R.id.txt2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp.getFlux().getActions().getUser();
            }
        });

        asyncBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp.getFlux().getActions().getUserAsync();
            }
        });
    }


    protected void onResume() {
        super.onResume();

        onChanged();
    }


    protected void onStop() {
        super.onStop();

        MyStore store = MyApp.getFlux().getStore(MyStore.class);
        store.removeListener(this);

        MyOtherStore otherStore = MyApp.getFlux().getStore(MyOtherStore.class);
        otherStore.removeListener(this);
    }

    @Override
    protected Class[] getStores() {
        return new Class[]{MyStore.class, MyOtherStore.class};
    }

    @Override
    protected Flux getFlux() {
        return MyApp.getFlux();
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
        final MyStore store = MyApp.getFlux().getStore(MyStore.class);
        final MyOtherStore.MyState state = MyApp.getFlux().getStore(MyOtherStore.class).getState();

        ThreadUtils.runOnMain(new Runnable() {
            @Override
            public void run() {
                txt.setText(store.getState());

                if(state.isLoading)
                    txt2.setText("Currently Loading User");
                else if(state.hasLoaded)
                    txt2.setText(state.user);
            }
        });
    }
}
