package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;

public class AddPropertyInformationCustomView extends ConstraintLayout {

    private EditText mAddPropertyInformationEditText;

    public AddPropertyInformationCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AddPropertyInformationCustomView);
        initView(attributes);
    }

    private void initView(@NonNull TypedArray attributes) {
        inflate(getContext(), R.layout.custom_view_add_property_information, this);

        ImageView addPropertyInformationIcon = findViewById(R.id.add_property_information_custom_view_icon);
        TextView addPropertyInformationTitle = findViewById(R.id.add_property_information_custom_view_title);
        mAddPropertyInformationEditText = findViewById(R.id.add_property_information_custom_view_value);

        addPropertyInformationIcon.setImageDrawable(attributes.getDrawable(R.styleable.AddPropertyInformationCustomView_icon));
        addPropertyInformationTitle.setText(attributes.getText(R.styleable.AddPropertyInformationCustomView_title));
        mAddPropertyInformationEditText.setInputType(attributes.getInt(R.styleable.AddPropertyInformationCustomView_android_inputType, InputType.TYPE_CLASS_NUMBER));
        mAddPropertyInformationEditText.setSingleLine(attributes.getBoolean(R.styleable.AddPropertyInformationCustomView_android_singleLine, true));
        mAddPropertyInformationEditText.setLines(attributes.getInt(R.styleable.AddPropertyInformationCustomView_android_lines, 1));
        attributes.recycle();
    }


    public void setValueText( String text) {
        mAddPropertyInformationEditText.setText(text);
        Log.i("TAG", "setValueText: "+mAddPropertyInformationEditText.getText()+" "+getContext().getResources().getResourceName(mAddPropertyInformationEditText.getId()));
    }

    @NonNull
    public String getValueForView() {
        return mAddPropertyInformationEditText.getText().toString();
    }

    @NonNull
    public  Boolean isEditTextEmpty() {
        return mAddPropertyInformationEditText.getText().toString().trim().isEmpty();
    }
}
