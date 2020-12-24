package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyInformationLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PhotoRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.RecyclerViewItemClickListener;
import com.openclassrooms.realestatemanager.presentation.utils.customView.AddPropertyInformationCustomView;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPropertyFragment extends BaseFragment {

    private FragmentAddPropertyBinding mBinding;
    private List<PropertyPhoto> mPropertyPhotos = new ArrayList<>();
    private List<PropertyPhoto> mPhotosToDelete = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddPropertyBinding.inflate(getLayoutInflater());
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        setAppBarVisibility(false);
        initRecyclerView();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPropertyViewModel.getPhotosToDelete.observe(getViewLifecycleOwner(), propertyPhotos ->
                mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setVisibility(propertyPhotos.isEmpty() ? View.GONE : View.VISIBLE));
        configureOnClickRecyclerView();
        initView();
        initClickListener();
        if (mPropertyViewModel.getSelectedProperty.getValue()!=null)
        initViewValueWhenUpdate(mBinding.addPropertyInformationLayout, mPropertyViewModel.getSelectedProperty.getValue());
        initDropDownMenu();
    }

    private void initView() {
        setUpdateButtonIcon(false);

        EditText editText = mBinding.addPropertyInformationLayout.addPropertyInformationAddress.findViewById(R.id.add_property_information_custom_view_value);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setSingleLine(false);
        editText.setLines(6);
    }

    private void initClickListener() {
        mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setOnClickListener(v ->
                Toast.makeText(requireContext(), "delete", Toast.LENGTH_SHORT).show());

        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.setOnClickListener(v -> {
            if (mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked())
                mBinding.addPropertySoldLayout.addPropertySoldEditText.setVisibility(View.VISIBLE);
            else mBinding.addPropertySoldLayout.addPropertySoldEditText.setVisibility(View.GONE);
        });
    }

    private void initViewValueWhenUpdate(FragmentAddPropertyInformationLayoutBinding addPropertyInformationCustomView, Property property) {
        mPropertyViewModel.setPhotosToDelete(new ArrayList<>());
        EditText descriptionEditText = mBinding.addPropertyDescriptionLayout.addPropertyDescriptionEditText;
        AutoCompleteTextView propertyTypeDropDownMenu = mBinding.addPropertyInformationLayout.addPropertyInformationTypeCustomViewAutocompleteTextView;
        if (property.getAddress() != null) {
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationPrice, String.valueOf(property.getPrice()));
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationArea, String.valueOf(property.getPropertyArea()));
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationNumberOfBathrooms, String.valueOf(property.getNumberOfBathrooms()));
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationNumberOfBedrooms, String.valueOf(property.getNumberOfBedrooms()));
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationNumberOfRooms, String.valueOf(property.getNumberOfRooms()));
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationAddress, property.getAddress());
            mPropertyPhotos.addAll(Objects.requireNonNull(mPropertyViewModel.getAllRoomPropertyPhotosForProperty.getValue()));
            descriptionEditText.setText(property.getDescription());
            propertyTypeDropDownMenu.setText(property.getPropertyType());
            }
    }

    private void initRecyclerView() {
        PropertyPhoto propertyPhoto = new PropertyPhoto(0, "AddPhoto", "add photo", 0);
        mPropertyPhotos.add(propertyPhoto);
        PhotoRecyclerViewAdapter adapter = new PhotoRecyclerViewAdapter(mPropertyPhotos);
        mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView.setAdapter(adapter);
    }

    private void setTextForCustomView(AddPropertyInformationCustomView customView, String text) {
        EditText editText = customView.findViewById(R.id.add_property_information_custom_view_value);
        editText.setText(text);
    }

    public void configureOnClickRecyclerView() {
        RecyclerViewItemClickListener.addTo(mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView, R.layout.fragment_add_property)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    if (position == 0)
                        Toast.makeText(requireContext(), "add", Toast.LENGTH_SHORT).show();
                });

        RecyclerViewItemClickListener.addTo(mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView, R.layout.fragment_add_property)
                .setOnItemLongClickListener((recyclerView, position, v) -> {

                    if (position==0)return false;

                    List<PropertyPhoto> basePhotos = mPropertyViewModel.getAllRoomPropertyPhotosForProperty.getValue();
                    int indexOnBasePhotos = position - 1;

                    CheckBox checkBox = v.findViewById(R.id.property_detail_item_check_box);
                    checkBox.setChecked(!checkBox.isChecked());
                    checkBox.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);

                    assert basePhotos != null;
                    if (checkBox.isChecked() && !mPhotosToDelete.contains(basePhotos.get(indexOnBasePhotos)))
                        mPhotosToDelete.add(basePhotos.get(indexOnBasePhotos));
                    else if (!mPhotosToDelete.isEmpty())
                        mPhotosToDelete.remove(basePhotos.get(indexOnBasePhotos));

                    mPropertyViewModel.setPhotosToDelete(mPhotosToDelete);

                    return true;
                });
    }

    //TODO replace property type by R.string enum?
    private void initDropDownMenu() {
        String[] propertyType = {"Flat", "House", "Duplex", "Penthouse"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, propertyType);
        AutoCompleteTextView propertyTypeTextView = (AutoCompleteTextView) mBinding.addPropertyInformationLayout.addPropertyInformationType.getEditText();
        assert propertyTypeTextView != null;
        propertyTypeTextView.setAdapter(adapter);
    }
}