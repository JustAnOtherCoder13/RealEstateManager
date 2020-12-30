package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.os.Bundle;
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
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.openclassrooms.realestatemanager.presentation.utils.customView.AddPropertyInformationCustomView.getValueForView;
import static com.openclassrooms.realestatemanager.presentation.utils.customView.AddPropertyInformationCustomView.setValueText;
import static com.picone.core.utils.ConstantParameters.ADD_PHOTO;
import static com.picone.core.utils.ConstantParameters.PROPERTY_TO_ADD;

public class AddPropertyFragment extends BaseFragment {

    private FragmentAddPropertyBinding mBinding;
    private List<PropertyPhoto> mPropertyPhotos = new ArrayList<>();
    private List<PropertyPhoto> mPhotosToDelete = new ArrayList<>();
    private boolean mIsPropertyValuesAreNull;
    private String mInitialLastPropertyAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddPropertyBinding.inflate(getLayoutInflater());
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        setAppBarVisibility(false);
        initRecyclerView();
        setUpdateButtonIcon(false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsPropertyValuesAreNull = Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()).getAddress() == null;
        mInitialLastPropertyAddress = Objects.requireNonNull(mPropertyViewModel.getAllProperties.getValue()).get(mPropertyViewModel.getAllProperties.getValue().size() - 1).getAddress();
        initView();
        initDropDownMenu();
        initClickListener();
        configureOnClickRecyclerView();

        mPropertyViewModel.getSelectedProperty.observe(getViewLifecycleOwner(), property -> {
            if (property.getAddress() != null)
                mPropertyViewModel.setPropertyLocationForProperty(mPropertyViewModel.getSelectedProperty.getValue());
        });

        mPropertyViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {
            switch (completionState) {
                case ADD_PROPERTY_COMPLETE:
                    setLocationForPropertyAddress();
                    break;

                case ADD_LOCATION_COMPLETE:
                    mNavController.navigate(R.id.action_addPropertyFragment_to_propertyListFragment);
            }
        });
    }

    private void setLocationForPropertyAddress() {

        mPropertyViewModel.getAllProperties.observe(getViewLifecycleOwner(), properties -> {
            String actualLastPropertyAddress = Objects.requireNonNull(mPropertyViewModel.getAllProperties.getValue()).get(mPropertyViewModel.getAllProperties.getValue().size() - 1).getAddress();
            if (!mInitialLastPropertyAddress.equals(actualLastPropertyAddress))
                mPropertyViewModel.setPropertyLocationForPropertyAddress(mPropertyViewModel.getAllProperties.getValue().get(mPropertyViewModel.getAllProperties.getValue().size() - 1));
        });

        mPropertyViewModel.getLocationForAddress.observe(getViewLifecycleOwner(), propertyLocation -> {
            if (propertyLocation.getPropertyId() != 0)
                mPropertyViewModel.addPropertyLocationForProperty(propertyLocation);
        });
    }
    //___________________________________VIEW_____________________________________________

    private void initRecyclerView() {
        PropertyPhoto propertyPhoto = new PropertyPhoto(0, ADD_PHOTO, "", 0);
        mPropertyPhotos.add(propertyPhoto);
        PhotoRecyclerViewAdapter adapter = new PhotoRecyclerViewAdapter(mPropertyPhotos);
        mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        if (!mIsPropertyValuesAreNull)
            initViewValueWhenUpdate(mBinding.addPropertyInformationLayout, Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()));

        mPropertyViewModel.getPhotosToDelete.observe(getViewLifecycleOwner(), photosToDelete ->
                mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setVisibility(photosToDelete.isEmpty() ? View.GONE : View.VISIBLE));

    }

    private void initViewValueWhenUpdate(@NonNull FragmentAddPropertyInformationLayoutBinding addPropertyInformationCustomView, @NonNull Property property) {
        mPropertyViewModel.setPhotosToDelete(new ArrayList<>());
        EditText descriptionEditText = mBinding.addPropertyDescriptionLayout.addPropertyDescriptionEditText;
        AutoCompleteTextView propertyTypeDropDownMenu = mBinding.addPropertyInformationLayout.addPropertyInformationTypeCustomViewAutocompleteTextView;
        setValueText(addPropertyInformationCustomView.addPropertyInformationPrice, String.valueOf(property.getPrice()));
        setValueText(addPropertyInformationCustomView.addPropertyInformationArea, String.valueOf(property.getPropertyArea()));
        setValueText(addPropertyInformationCustomView.addPropertyInformationNumberOfBathrooms, String.valueOf(property.getNumberOfBathrooms()));
        setValueText(addPropertyInformationCustomView.addPropertyInformationNumberOfBedrooms, String.valueOf(property.getNumberOfBedrooms()));
        setValueText(addPropertyInformationCustomView.addPropertyInformationNumberOfRooms, String.valueOf(property.getNumberOfRooms()));
        setValueText(addPropertyInformationCustomView.addPropertyInformationAddress, property.getAddress());
        mPropertyPhotos.addAll(Objects.requireNonNull(mPropertyViewModel.getAllPropertyPhotosForProperty.getValue()));
        descriptionEditText.setText(property.getDescription());
        propertyTypeDropDownMenu.setText(property.getPropertyType());
    }

    private void initDropDownMenu() {
        String[] propertyType = getResources().getStringArray(R.array.property_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, propertyType);
        AutoCompleteTextView propertyTypeTextView = (AutoCompleteTextView) mBinding.addPropertyInformationLayout.addPropertyInformationType.getEditText();
        assert propertyTypeTextView != null;
        propertyTypeTextView.setAdapter(adapter);
    }

    //___________________________________CLICK LISTENER_____________________________________________

    private void initClickListener() {
        mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setOnClickListener(v ->
                Toast.makeText(requireContext(), "delete", Toast.LENGTH_SHORT).show());

        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.setOnClickListener(v ->
                mBinding.addPropertySoldLayout.addPropertySoldEditText.setVisibility(
                        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked() ?
                                View.VISIBLE : View.GONE));

        mUpdateButton.setOnClickListener(v -> addProperty());

    }

    private void addProperty() {
        if (!isRequiredInformationAreFilled()) {
            if (mIsPropertyValuesAreNull)
                mPropertyViewModel.addProperty(updatedProperty(PROPERTY_TO_ADD(Objects.requireNonNull(mAgentViewModel.getAgent.getValue()))));
            else
                mPropertyViewModel.updateProperty(updatedProperty(Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue())));

        } else Toast.makeText(requireContext(), "fill it", Toast.LENGTH_SHORT).show();
    }

    private boolean isRequiredInformationAreFilled() {
        return getValueForView(mBinding.addPropertyInformationLayout.addPropertyInformationAddress).trim().isEmpty();
    }

    public void configureOnClickRecyclerView() {
        initAddMediaClickListener();
        initSelectPhotoToDeleteOnLongClickListener();
    }

    private void initSelectPhotoToDeleteOnLongClickListener() {
        RecyclerViewItemClickListener.addTo(mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView, R.layout.fragment_add_property)
                .setOnItemLongClickListener((recyclerView, position, v) -> {
                    if (position == 0) return false;
                    CheckBox selectToDeleteCheckBox = v.findViewById(R.id.property_detail_item_check_box);
                    selectToDeleteCheckBox.setChecked(!selectToDeleteCheckBox.isChecked());
                    selectToDeleteCheckBox.setVisibility(selectToDeleteCheckBox.isChecked() ? View.VISIBLE : View.GONE);
                    setPhotoToDeleteList(position, selectToDeleteCheckBox);
                    return true;
                });
    }

    private void initAddMediaClickListener() {
        RecyclerViewItemClickListener.addTo(mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView, R.layout.fragment_add_property)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    if (position == 0)
                        Toast.makeText(requireContext(), "add", Toast.LENGTH_SHORT).show();
                });
    }

    //___________________________________HELPERS_____________________________________________

    @NonNull
    @Contract("_ -> param1")
    private Property updatedProperty(@NonNull Property property) {
        property.setAddress(getValueForView(mBinding.addPropertyInformationLayout.addPropertyInformationAddress));
        property.setNumberOfRooms(Integer.parseInt(getValueForView(mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfRooms)));
        property.setNumberOfBathrooms(Integer.parseInt((getValueForView(mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfBathrooms))));
        property.setNumberOfBedrooms(Integer.parseInt(getValueForView(mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfBedrooms)));
        property.setPropertyArea(Integer.parseInt(getValueForView(mBinding.addPropertyInformationLayout.addPropertyInformationArea)));
        property.setPrice(Integer.parseInt(getValueForView(mBinding.addPropertyInformationLayout.addPropertyInformationPrice)));
        property.setPropertyType(mBinding.addPropertyInformationLayout.addPropertyInformationTypeCustomViewAutocompleteTextView.getText().toString());
        property.setDescription(mBinding.addPropertyDescriptionLayout.addPropertyDescriptionEditText.getText().toString());
        property.setSold(mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked());
        property.setSoldFrom(mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked() ? mBinding.addPropertySoldLayout.addPropertySoldEditText.getText().toString() : "");
        return property;
    }

    private void setPhotoToDeleteList(int position, @NonNull CheckBox selectToDeleteCheckBox) {
        int indexForExistingMedia = position - 1;
        List<PropertyPhoto> existingMediaForProperty = mPropertyViewModel.getAllPropertyPhotosForProperty.getValue();
        assert existingMediaForProperty != null;
        if (selectToDeleteCheckBox.isChecked() && !mPhotosToDelete.contains(existingMediaForProperty.get(indexForExistingMedia)))
            mPhotosToDelete.add(existingMediaForProperty.get(indexForExistingMedia));
        else if (!mPhotosToDelete.isEmpty())
            mPhotosToDelete.remove(existingMediaForProperty.get(indexForExistingMedia));
        mPropertyViewModel.setPhotosToDelete(mPhotosToDelete);
    }
}