package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;

public class AddPropertyInformationCustomView extends ConstraintLayout {


    public AddPropertyInformationCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        inflate(getContext(), R.layout.custom_view_add_property_information, this);

        ImageView addPropertyInformationIcon = findViewById(R.id.add_property_information_custom_view_icon);
        TextView addPropertyInformationTitle = findViewById(R.id.add_property_information_custom_view_title);
        //EditText addPropertyInformationEditText = findViewById(R.id.add_property_information_custom_view_value);

        TypedArray attributes = getContext().obtainStyledAttributes(attrs,R.styleable.AddPropertyInformationCustomView);

        addPropertyInformationIcon.setImageDrawable(attributes.getDrawable(R.styleable.AddPropertyInformationCustomView_icon));
        addPropertyInformationTitle.setText(attributes.getText(R.styleable.AddPropertyInformationCustomView_title));
        //addPropertyInformationEditText.setInputType(R.styleable.AddPropertyInformationCustomView_android_inputType);
        attributes.recycle();
    }
}
