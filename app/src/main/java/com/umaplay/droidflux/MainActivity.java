package com.umaplay.droidflux;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umaplay.fluxxan.Flux;
import com.umaplay.fluxxan.ui.ReducerListenerActivity;
import com.umaplay.fluxxan.utils.ThreadUtils;


@SuppressWarnings("ConstantConditions")
public class MainActivity extends ReducerListenerActivity<MyState> {

    private TextView txt;

    private TextView txt2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btn);
        Button asyncBtn = (Button) findViewById(R.id.asyncBtn);
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


        Button btn2 = (Button) findViewById(R.id.btn2);
        Button asyncBtn2 = (Button) findViewById(R.id.asyncBtn2);
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

        onStateChanged(MyApp.getFlux().getState());
    }



    @Override
    protected Flux<MyState, MyActions> getFlux() {
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
    public boolean hasStateChanged(MyState newState, MyState oldState) {
        return true;
    }

    @Override
    public void onStateChanged(final MyState state) {

        ThreadUtils.runOnMain(new Runnable() {
            @Override
            public void run() {
                txt.setText(state.StateOne);

                if(state.StateTwo.isLoading)
                    txt2.setText("Currently Loading User");
                else if(state.StateTwo.hasLoaded)
                    txt2.setText(state.StateTwo.user);
            }
        });
    }
}
