package com.umaplay.fluxxandemo.flux.action;

import com.umaplay.fluxxan.Payload;
import com.umaplay.fluxxan.impl.BaseActions;
import com.umaplay.fluxxandemo.flux.model.AppState;
import com.umaplay.fluxxandemo.flux.model.Todo;

/**
 * Created by user on 5/8/2015.
 */
public class TodoActions extends BaseActions {
    public static final String ADD_TODO = "ADD_TODO";
    public static final String CLOSE_TODO = "CLOSE_TODO";
    public static final String OPEN_TODO = "OPEN_TODO";
    public static final String DELETE_TODO = "DELETE_TODO";
    public static final String CHANGE_VISIBILITY = "CHANGE_VISIBILITY";

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

    public void changeVisibility(AppState.Visibility state) {
        dispatch(Creator.changeVisibility(state));
    }

    public static class Creator {
        public static Payload<String> addTodo(String todo) {
            return new Payload<>(ADD_TODO, todo);
        }

        public static Payload<Todo> openTodo(Todo todo) {
            return new Payload<>(OPEN_TODO, todo);
        }

        public static Payload<Todo> closeTodo(Todo todo) {
            return new Payload<>(CLOSE_TODO, todo);
        }

        public static Payload<Todo> deleteTodo(Todo todo) {
            return new Payload<>(DELETE_TODO, todo);
        }

        public static Payload<AppState.Visibility> changeVisibility(AppState.Visibility state) {
            return new Payload<>(CHANGE_VISIBILITY, state);
        }
    }
}
