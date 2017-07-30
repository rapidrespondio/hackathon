package com.example.winningteam.rapidrespose;

import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

public class test extends AppCompatActivity {



        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("clicked")
        private int mClicked;


        public test() {}


        public test(String id, String name, int clicked) {
            this.id = id;
            this.name = name;
            this.mClicked = clicked;
        }
        @Bindable
        public int getClicked() {
            return mClicked;
        }


        public void setClicked(int newValue) {
            mClicked = newValue;

        }
    }
