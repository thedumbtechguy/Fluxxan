package com.umaplay.fluxxandemo.flux.reducer;

import com.umaplay.fluxxan.annotation.BindAction;
import com.umaplay.fluxxan.impl.BaseAnnotatedReducer;
import com.umaplay.fluxxandemo.flux.action.TodoActions;
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
public class TodoReducer extends BaseAnnotatedReducer<ImmutableAppState> {

    @BindAction(TodoActions.ADD_TODO)
    public ImmutableAppState addTodo(ImmutableAppState state, String todo) {
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

    @BindAction(TodoActions.OPEN_TODO)
    public ImmutableAppState openTodo(ImmutableAppState state, Todo todo) {
        Todo iTodo = ImmutableTodo.builder()
                .from(todo)
                .status(Todo.Status.OPEN)
                .build();


        return ImmutableAppState.builder()
                .from(state)
                .putTodos(iTodo.getUid(), iTodo)
                .build();
    }

    @BindAction(TodoActions.CLOSE_TODO)
    public ImmutableAppState closeTodo(ImmutableAppState state, Todo todo) {
        Todo iTodo = ImmutableTodo.builder()
                .from(todo)
                .status(Todo.Status.CLOSED)
                .build();


        return ImmutableAppState.builder()
                .from(state)
                .putTodos(iTodo.getUid(), iTodo)
                .build();
    }


    @BindAction(TodoActions.DELETE_TODO)
    public ImmutableAppState deleteTodo(ImmutableAppState state, Todo todo) {

        Map<String, Todo> todos = new LinkedHashMap<>(state.getTodos());
        todos.remove(todo.getUid());

        return ImmutableAppState.builder()
                .from(state)
                .todos(todos)
                .build();
    }

    @BindAction(TodoActions.CHANGE_VISIBILITY)
    public ImmutableAppState changeVisibiity(ImmutableAppState state, AppState.Visibility visiblilty) {
        return ImmutableAppState.builder()
                .from(state)
                .visibility(visiblilty)
                .build();
    }

}
