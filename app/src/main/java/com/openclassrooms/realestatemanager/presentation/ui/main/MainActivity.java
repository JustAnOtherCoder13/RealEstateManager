package com.openclassrooms.realestatemanager.presentation.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.presentation.utils.Utils;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
