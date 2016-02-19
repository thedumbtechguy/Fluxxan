package com.umaplay.fluxxandemo.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.ui.StateListenerActivity;
import com.umaplay.fluxxan.util.ThreadUtils;
import com.umaplay.fluxxandemo.App;
import com.umaplay.fluxxandemo.R;
import com.umaplay.fluxxandemo.adapter.TodoListAdapter;
import com.umaplay.fluxxandemo.flux.actioncreator.TodoActionCreator;
import com.umaplay.fluxxandemo.flux.model.AppState;
import com.umaplay.fluxxandemo.flux.model.Todo;

import java.util.ArrayList;

public class TodoListActivity extends StateListenerActivity<AppState, TodoActionCreator> {

    private TodoListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new TodoListAdapter(this, new ArrayList<Todo>());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.todosList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewInflated = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.text_input,
                        (ViewGroup) TodoListActivity.this.findViewById(R.id.root), false);
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);

                AlertDialog.Builder builder = new AlertDialog.Builder(TodoListActivity.this);
                builder.setTitle("Add Todo");
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = input.getText().toString();
                        getFlux().getActionCreator().addTodo(text);

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todos, menu);
        menu.findItem(R.id.change_visibility).setTitle("SHOW " + getFlux().getState().getFilter().toString());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.change_visibility:
                AppState.Filter filter = getFlux().getState().getFilter();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Filter");


                //again, we cheat here when we should be using a proper menu.
                //we are like little klepto banditos

                int i = 0;
                String[] items = new String[2];

                final AppState.Filter[] states = new AppState.Filter[2];

                if(!filter.equals(AppState.Filter.ALL)) {
                    items[i] = "All Todos";
                    states[i] = AppState.Filter.ALL;
                    i++;
                }
                if(!filter.equals(AppState.Filter.OPEN)) {
                    items[i] = "Open Todos";
                    states[i] = AppState.Filter.OPEN;
                    i++;
                }
                if(!filter.equals(AppState.Filter.CLOSED)) {
                    items[i] = "Closed Todos";
                    states[i] = AppState.Filter.CLOSED;
                    i++;
                }

                builder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getFlux().getActionCreator().changeVisibility(states[which]);
                            }
                        });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Fluxxan<AppState, TodoActionCreator> getFlux() {
        return App.getFlux();
    }

    @Override
    public void onStateChanged(final AppState appState) {
        ArrayList<Todo> showTodos;


        AppState.Filter vb = appState.getFilter();
        if(vb.equals(AppState.Filter.ALL)) {
            showTodos = new ArrayList<>(appState.getTodos().values());
        }
        else {
            showTodos = new ArrayList<Todo>();

            for (Todo todo : appState.getTodos().values()) {
                if(todo.getStatus().equals(Todo.Status.CLOSED) && vb.equals(AppState.Filter.CLOSED))
                    showTodos.add(todo);
                else if(todo.getStatus().equals(Todo.Status.OPEN) && vb.equals(AppState.Filter.OPEN))
                    showTodos.add(todo);
            }
        }

        final ArrayList<Todo> finalShowTodos = showTodos;
        ThreadUtils.runOnMain(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateData(finalShowTodos);
                supportInvalidateOptionsMenu();
            }
        });
    }
}
