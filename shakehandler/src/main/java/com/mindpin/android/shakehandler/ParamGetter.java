package com.mindpin.android.shakehandler;

/**
 * Created by dd on 14-7-3.
 */
public abstract class ParamGetter<T> {
    final T t;

    public ParamGetter(T t) {
        this.t = t;
    }

    public abstract String get_param_value(T t);
}