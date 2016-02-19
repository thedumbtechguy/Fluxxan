package com.umaplay.fluxxandemo.flux.actioncreator;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseActionCreator;
import com.umaplay.fluxxandemo.flux.model.AppState;
import com.umaplay.fluxxandemo.flux.model.Todo;

/**
 * Created by user on 5/8/2015.
 */
public class TodoActionCreator extends BaseActionCreator {
    public static final String ADD_TODO = "ADD_TODO";
    public static final String CLOSE_TODO = "CLOSE_TODO";
    public static final String OPEN_TODO = "OPEN_TODO";
    public static final String DELETE_TODO = "DELETE_TODO";
    public static final String CHANGE_FILTER = "CHANGE_FILTER";

    public void addTodo(String todo) {
        dispatch(Creator.addTodo(todo));
    }

    public void openTodo(Todo todo) {
        dispatch(Creator.openTodo(todo));
    }

    public void closeTodo(Todo todo) {
        dispatch(Creator.closeTodo(todo));
    }

    public void deleteTodo(Todo todo) {
        dispatch(Creator.deleteTodo(todo));
    }

    public void changeVisibility(AppState.Filter state) {
        dispatch(Creator.changeVisibility(state));
    }

    public static class Creator {
        public static Action<String> addTodo(String todo) {
            return new Action<>(ADD_TODO, todo);
        }

        public static Action<Todo> openTodo(Todo todo) {
            return new Action<>(OPEN_TODO, todo);
        }

        public static Action<Todo> closeTodo(Todo todo) {
            return new Action<>(CLOSE_TODO, todo);
        }

        public static Action<Todo> deleteTodo(Todo todo) {
            return new Action<>(DELETE_TODO, todo);
        }

        public static Action<AppState.Filter> changeVisibility(AppState.Filter state) {
            return new Action<>(CHANGE_FILTER, state);
        }
    }
}
