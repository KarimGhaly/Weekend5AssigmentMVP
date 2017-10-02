package com.example.admin.weekend5assigmentmvp;

import android.view.View;

/**
 * Created by Admin on 10/1/2017.
 */

public interface BasePresenter<V extends BaseView> {
    void attach(V view);
    void detach();
}
