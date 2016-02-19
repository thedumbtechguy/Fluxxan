package com.umaplay.fluxxandemo.flux.reducer;

import com.umaplay.fluxxan.annotation.BindAction;
import com.umaplay.fluxxan.impl.BaseAnnotatedReducer;
import com.umaplay.fluxxandemo.flux.actioncreator.TodoActionCreator;
import com.umaplay.fluxxandemo.flux.model.AppState;
import com.umaplay.fluxxandemo.flux.model.ImmutableAppState;
import com.umaplay.fluxxandemo.flux.model.ImmutableTodo;
import com.umaplay.fluxxandemo.flux.model.Todo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by user on 5/8/2015.
 */
public class TodoReducer extends BaseAnnotatedReducer<AppState> {

    @BindAction(TodoActionCreator.ADD_TODO)
    public AppState addTodo(AppState state, String todo) {
        Todo iTodo = ImmutableTodo.builder()
                .uid(UUID.randomUUID().toString())
                .title(todo)
                .status(Todo.Status.OPEN)
                .build();

        return ImmutableAppState.builder()
                .from(state)
                .putTodos(iTodo.getUid(), iTodo)
                .build();
    }

    @BindAction(TodoActionCreator.OPEN_TODO)
    public AppState openTodo(AppState state, Todo todo) {
        Todo iTodo = ImmutableTodo.builder()
                .from(todo)
                .status(Todo.Status.OPEN)
                .build();

        return ImmutableAppState.builder()
                .from(state)
                .putTodos(iTodo.getUid(), iTodo)
                .build();
    }

    @BindAction(TodoActionCreator.CLOSE_TODO)
    public AppState closeTodo(AppState state, Todo todo) {
        Todo iTodo = ImmutableTodo.builder()
                .from(todo)
                .status(Todo.Status.CLOSED)
                .build();


        return ImmutableAppState.builder()
                .from(state)
                .putTodos(iTodo.getUid(), iTodo)
                .build();
    }


    @BindAction(TodoActionCreator.DELETE_TODO)
    public AppState deleteTodo(AppState state, Todo todo) {

        Map<String, Todo> todos = new LinkedHashMap<>(state.getTodos());
        todos.remove(todo.getUid());

        return ImmutableAppState.builder()
                .from(state)
                .todos(todos)
                .build();
    }

    @BindAction(TodoActionCreator.CHANGE_FILTER)
    public AppState changeFilter(AppState state, AppState.Filter filter) {
        return ImmutableAppState.builder()
                .from(state)
                .filter(filter)
                .build();
    }

}
