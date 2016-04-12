package com.umaplay.fluxxandemo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umaplay.fluxxandemo.App;
import com.umaplay.fluxxandemo.R;
import com.umaplay.fluxxandemo.flux.actioncreator.TodoActionCreator;
import com.umaplay.fluxxandemo.flux.model.Todo;

import java.util.List;

/**
 * Created by user on 2/17/2016.
 */
public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {
    private List<Todo> todoList;
    private Context mContext;

    public TodoListAdapter(Context context, List<Todo> todoList) {
        this.todoList = todoList;
        this.mContext = context;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todoitem, null);

        TodoViewHolder viewHolder = new TodoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodoViewHolder todoViewHolder, int i) {
        final Todo todo = todoList.get(i);

        //Setting text view title
        todoViewHolder.textView.setText(todo.getTitle());
        if(todo.getStatus().equals(Todo.Status.CLOSED)) {
            todoViewHolder.textView.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
            todoViewHolder.textView.setTextColor(mContext.getResources().getColor(android.R.color.white));
        }
        else {
            todoViewHolder.textView.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            todoViewHolder.textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
        }

        todoViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(todo.getTitle());
                //we cheat here when we should be using a proper menu
                builder.setItems(new String[]{todo.getStatus().equals(Todo.Status.OPEN) ? "Close" : "Open", "Delete"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        if(todo.getStatus().equals(Todo.Status.OPEN))
                                            TodoActionCreator.instance().closeTodo(todo);
                                        else
                                            TodoActionCreator.instance().openTodo(todo);
                                        break;
                                    case 1:
                                        TodoActionCreator.instance().deleteTodo(todo);
                                        break;
                                }
                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != todoList ? todoList.size() : 0);
    }

    public void updateData(List<Todo> list) {
        todoList = list;
        notifyDataSetChanged();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        protected TextView textView;

        public TodoViewHolder(View view) {
            super(view);

            this.view = view;
            this.textView = (TextView) view.findViewById(R.id.title);
        }
    }
}

