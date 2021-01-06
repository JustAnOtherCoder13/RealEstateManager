package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;

public class AddPropertyInformationCustomView extends ConstraintLayout {

    private ImageView mAddPropertyInformationIcon;
    private TextView mAddPropertyInformationTitle;
    private EditText mAddPropertyInformationEditText;

    public AddPropertyInformationCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AddPropertyInformationCustomView);
        initView(attributes);
    }

    private void initView(TypedArray attributes) {
        inflate(getContext(), R.layout.custom_view_add_property_information, this);

        mAddPropertyInformationIcon = findViewById(R.id.add_property_information_custom_view_icon);
        mAddPropertyInformationTitle = findViewById(R.id.add_property_information_custom_view_title);
        mAddPropertyInformationEditText = findViewById(R.id.add_property_information_custom_view_value);

        mAddPropertyInformationIcon.setImageDrawable(attributes.getDrawable(R.styleable.AddPropertyInformationCustomView_icon));
        mAddPropertyInformationTitle.setText(attributes.getText(R.styleable.AddPropertyInformationCustomView_title));
        mAddPropertyInformationEditText.setInputType(attributes.getInt(R.styleable.AddPropertyInformationCustomView_android_inputType, InputType.TYPE_CLASS_NUMBER));
        mAddPropertyInformationEditText.setSingleLine(attributes.getBoolean(R.styleable.AddPropertyInformationCustomView_android_singleLine, true));
        mAddPropertyInformationEditText.setLines(attributes.getInt(R.styleable.AddPropertyInformationCustomView_android_lines, 1));
        attributes.recycle();
    }


    public static void setValueText(@NonNull View view, String text) {
        EditText editText = view.findViewById(R.id.add_property_information_custom_view_value);
        editText.setText(text);
    }


    @NonNull
    public static String getValueForView(@NonNull View view) {
        EditText editText = view.findViewById(R.id.add_property_information_custom_view_value);
        return editText.getText().toString();
    }

    @NonNull
    public static Boolean isEditTextEmpty(@NonNull View view) {
        EditText editText = view.findViewById(R.id.add_property_information_custom_view_value);
        return editText.getText().toString().trim().isEmpty();
    }
}
