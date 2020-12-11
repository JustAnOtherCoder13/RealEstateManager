package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;

public class AddPropertyInformationTypeCustomView extends ConstraintLayout {
    public AddPropertyInformationTypeCustomView(@NonNull Context context) {
        super(context);
    }

    public AddPropertyInformationTypeCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AddPropertyInformationTypeCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AddPropertyInformationTypeCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View addPropertyInformationType = LayoutInflater.from(getContext()).inflate(R.layout.custom_view_add_property_information_type,this,false);
        addView(addPropertyInformationType);
    }
}
