package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.presentation.ui.main.MainActivity;

import java.util.Objects;

public class TopAppBarCustomView extends ConstraintLayout {

    public TopAppBarCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        inflate(getContext(), R.layout.custom_view_top_nav_bar, this);
        ImageButton addPropertyButton = findViewById(R.id.top_bar_add_property);
        addPropertyButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController((MainActivity)context,R.id.nav_host_fragment);
            switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()){
                case R.id.propertyListFragment:
                    navController.navigate
                            (R.id.action_propertyListFragment_to_addPropertyFragment);
                    break;

                case R.id.mapsFragment:
                    navController.navigate
                            (R.id.action_mapsFragment_to_addPropertyFragment);
                    break;

                default:
                    navController.navigate
                            (R.id.addPropertyFragment);
            }
        });
    }
}
