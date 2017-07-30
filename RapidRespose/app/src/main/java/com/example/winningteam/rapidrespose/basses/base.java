package com.example.winningteam.rapidrespose.basses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.alfonz.mvvm.AlfonzView;

import eu.inloop.viewmodel.binding.ViewModelBindingConfig;


public abstract class base extends AppCompatActivity implements AlfonzView {
    @Override
    public Bundle getExtras() {
        return null;
    }


    @Nullable
    @Override
    public ViewModelBindingConfig getViewModelBindingConfig() {
        return null;
    }


    @Override
    public void removeViewModel() {

    }
}